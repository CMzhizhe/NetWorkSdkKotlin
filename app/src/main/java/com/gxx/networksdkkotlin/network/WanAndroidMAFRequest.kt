package com.gxx.networksdkkotlin.network

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.gxx.networksdkkotlin.MyApplication.Companion.HTTP_NAME
import com.gxx.networksdkkotlin.network.api.CustomApiService
import com.gxx.networksdkkotlin.network.constant.Constant.Companion.ERROR_CODE_100
import com.gxx.networksdkkotlin.network.constant.Constant.Companion.ERROR_CODE_101
import com.gxx.networksdkkotlin.network.constant.Constant.Companion.ERROR_CODE_102
import com.gxx.networksdkkotlin.network.error.exception.LoginApiException
import com.gxx.networksdkkotlin.network.error.exception.PayApiException
import com.gxx.networksdkkotlin.network.error.exception.TokenApiException
import com.gxx.networksdkkotlin.network.error.handler.LoginErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.PayErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.TokenErrorHandler
import com.gxx.networksdkkotlin.network.interceptor.SortInterceptor
import com.gxx.networksdkkotlin.network.transform.ServiceDataTransform
import com.gxx.networksdkkotlin.util.AssetsHttpConfigRead
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.networksdkkotlin.network.parse.ServiceDataParseCall
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.NetWorkExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnCommonParamsListener
import com.gxx.neworklibrary.inter.OnOkHttpInterceptorListener
import com.gxx.neworklibrary.model.HttpConfigModel
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Interceptor
import org.json.JSONObject

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 用户自定义的 一个网络请求
 **/
object WanAndroidMAFRequest : ErrorHandlerFactory.OnServiceCodeErrorHandleFinishListener,
    ErrorHandlerFactory.OnNetWorkErrorListener {
    val TAG = "WanAndroidMAFRequest"
    val mMobileRequest: MobileRequest = MobileRequest(ServiceDataTransform())
    private val mInterceptor = mutableListOf<Interceptor>(SortInterceptor())
    var mHostUrl = ""

    fun init(application: Application){
        val httpJson = AssetsHttpConfigRead.readHttpConfig(application, HTTP_NAME)
        if (httpJson.isEmpty()){
            throw IllegalStateException("httpJson 是空的")
        }

        val jsonObject = JSONObject(httpJson)
        val httpConfigModel = GsonUtils.fromJson<HttpConfigModel>(jsonObject.getJSONObject("httpBusiness").toString(),HttpConfigModel::class.java)
        if (httpConfigModel.hostUrl.isEmpty()){
            throw IllegalStateException("hostUrl 是空的")
        }
        mHostUrl = httpConfigModel.hostUrl

        OkHttpManager.Builder()
            .setApplication(application)
            .setShowHttpLog(true)
            .setHttpConfig(HttpConfigModel(mHostUrl).apply {
                //拦截器
                onOkHttpInterceptorListener = object : OnOkHttpInterceptorListener {
                    override fun normalInterceptors(): List<Interceptor> {
                        //todo 可以在这里加解密等处理
                        return mInterceptor
                    }

                    override fun netWorkInterceptors(): List<Interceptor> {
                        return mutableListOf()
                    }

                }
                //公共参数
                onCommonParamsListener =  object : OnCommonParamsListener{
                    override fun onCommonParams(): LinkedHashMap<String, Any> {
                        return LinkedHashMap<String, Any>().apply {
                            put("age","12")
                            put("sexList", mutableListOf("female","male"))
                        }
                    }
                }
                //自定义错误
                errorHandlerFactory = ErrorHandlerFactory.Builder()
                    .addErrorHandler(LoginErrorHandler(),LoginApiException(ERROR_CODE_100.toString()))
                    .addErrorHandler(PayErrorHandler(),PayApiException(ERROR_CODE_101.toString()))
                    .addErrorHandler(TokenErrorHandler(),TokenApiException(ERROR_CODE_102.toString()))
                    .setOnNetWorkErrorListener(this@WanAndroidMAFRequest)
                    .setOnServiceCodeErrorHandleFinishListener(this@WanAndroidMAFRequest)
                    .build()
            })
            .build()
    }



    suspend fun <T> postRequest(
        funName: String,
        model:Any,
        urlMap: Map<String, Any>? = mutableMapOf(),
        serviceDataParseCall: ServiceDataParseCall<T>?
    ){
        mMobileRequest.postBody(
            RqParamModel(
                hostUrl = mHostUrl ,
                funName = funName,
                jsonBodyModel = GsonUtils.toJson(model),
                urlMap = urlMap
            ), serviceDataParseCall, serviceDataParseCall
        )
    }

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        serviceDataParseCall: ServiceDataParseCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                hostUrl = mHostUrl ,
                funName = funName,
                null,
                urlMap = urlMap
            ), serviceDataParseCall, serviceDataParseCall
        )
    }





    private val mJsonParseResult = JsonParseResult()

    /**
     * @date 创建时间: 2023/8/7
     * @auther gxx
     * 自定义api请求的Demo
     **/
    suspend fun <T> readBannerJson(serviceDataParseCall: ServiceDataParseCall<T>) {
        val api = OkHttpManager.getApi(mHostUrl, CustomApiService::class.java)
        val url = "${mHostUrl}banner/json"
        val responseBody = api?.readBook(url, mutableMapOf())
        mMobileRequest.responseBodyTransformJson(
            mHostUrl,
            "banner/json",
            responseBody,
            serviceDataParseCall
        ).collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${mHostUrl}banner/json",
                EmResultType.REQUEST_RESULT_OWN,
                listener = it,
                serviceDataParseCall,
                serviceDataParseCall
            )
        }
    }

    override fun onNetWorkError(throwable: NetWorkExceptionHandle.ResponeThrowable) {
        if(BuildConfig.DEBUG){
         Log.d(TAG, "onNetWorkError->网络错误,code=${throwable.code},msg=${throwable.message}");
        }
    }

    override fun onServiceCodeErrorHandleFinish(error: AbsApiException) {
       if(BuildConfig.DEBUG){
        Log.d(TAG, "onServiceCodeErrorHandleFinish->服务器给的错误，code=${error.code},msg=${error.errorMessage},jsString=${error.jsString}");
       }
    }
}