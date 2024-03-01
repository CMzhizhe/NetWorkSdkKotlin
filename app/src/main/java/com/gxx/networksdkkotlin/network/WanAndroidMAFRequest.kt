package com.gxx.networksdkkotlin.network

import android.os.Parcelable
import android.util.Log
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
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.base.doservicedata.parse.BaseServiceDataParseCall
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.NetWorkExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnCommonParamsListener
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.provider.HttpConfigInitializer
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 用户自定义的 一个网络请求
 **/
object WanAndroidMAFRequest : ErrorHandlerFactory.OnServiceCodeErrorHandleFinishListener,
    ErrorHandlerFactory.OnNetWorkErrorListener {
    val TAG = "WanAndroidMAFRequest"
    val mMobileRequest: MobileRequest = MobileRequest()
    val mHostUrl :String
    init {
        val bean = OkHttpManager.getInstance().getConfig(HttpConfigInitializer.HTTP_TYPE_HTTP_BUSINESS)
            ?: throw IllegalStateException("bean == null")
        mHostUrl = bean.hostUrl
        val retrofit = OkHttpManager.getInstance().createRetrofit(bean,OkHttpManager.Builder().apply {
            mIsShowNetHttpLog = BuildConfig.DEBUG
            mOnCommonParamsListener = object : OnCommonParamsListener{
                override fun onCommonParams(): LinkedHashMap<String, Any> {
                    return LinkedHashMap<String, Any>().apply {
                        put("age","12")
                        put("sexList", mutableListOf("female","male"))
                    }
                }
            }
            mErrorHandlerFactory =  ErrorHandlerFactory.Builder()
                .setHostUrl(bean.hostUrl)
                .addErrorHandler(LoginErrorHandler(),LoginApiException(ERROR_CODE_100.toString()))
                .addErrorHandler(PayErrorHandler(),PayApiException(ERROR_CODE_101.toString()))
                .addErrorHandler(TokenErrorHandler(),TokenApiException(ERROR_CODE_102.toString()))
                .setOnNetWorkErrorListener(this@WanAndroidMAFRequest)
                .setOnServiceCodeErrorHandleFinishListener(this@WanAndroidMAFRequest)
                .build()
        })
        OkHttpManager.getInstance().putRetrofit(mHostUrl,retrofit)
    }

    suspend fun <T,M:Parcelable> postRequest(
        model:M,
        funName: String,
        urlMap: Map<String, Any>? = mutableMapOf(),
        baseServiceDataParseCall: BaseServiceDataParseCall<T>?
    ){
        mMobileRequest.postBody(
            RqParamModel(
                hostUrl = mHostUrl ,
                funName = funName,
                model,
                urlMap = urlMap
            ), baseServiceDataParseCall, baseServiceDataParseCall
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
        baseServiceDataParseCall: BaseServiceDataParseCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                hostUrl = mHostUrl ,
                funName = funName,
                null,
                urlMap = urlMap
            ), baseServiceDataParseCall, baseServiceDataParseCall
        )
    }

    /**
     * @date 创建时间: 2023/8/10
     * @auther gaoxiaoxiong
     * @description flow方式请求
     **/
    suspend inline fun <reified T> createRequestFlow(funName: String) = callbackFlow<T> {
        val baseServiceDataParseCall = object : BaseServiceDataParseCall<T>() {
            override fun onRequestDataSuccess(data: T?) {
                super.onRequestDataSuccess(data)
                trySend(data!!)
            }
        }
        mMobileRequest.get(
            RqParamModel(
                hostUrl = mHostUrl,
                funName = funName,
                null,
                urlMap = mutableMapOf()
            ), baseServiceDataParseCall, baseServiceDataParseCall
        )
        awaitClose {}
    }



    private val mJsonParseResult = JsonParseResult()

    /**
     * @date 创建时间: 2023/8/7
     * @auther gxx
     * 自定义api请求的Demo
     **/
    suspend fun <T> readBannerJson(baseServiceDataParseCall: BaseServiceDataParseCall<T>) {
        val api = OkHttpManager.getInstance().getApi(mHostUrl, CustomApiService::class.java)
        val url = "${mHostUrl}banner/json"
        val responseBody = api?.readBook(url, mutableMapOf())
        mMobileRequest.responseBodyTransformJson(
            mHostUrl,
            "banner/json",
            responseBody,
            baseServiceDataParseCall
        ).collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${mHostUrl}banner/json",
                EmResultType.REQUEST_RESULT_OWN,
                listener = it,
                baseServiceDataParseCall,
                baseServiceDataParseCall
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