package com.gxx.networksdkkotlin.network

import android.app.Application
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
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.doservice.parse.BaseServiceDataParseSuFaCall
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 用户自定义的 一个网络请求
 **/
object WanAndroidMAFRequest : ErrorHandlerFactory.OnServiceCodeErrorHandleFinishListener,
    ErrorHandlerFactory.OnNetWorkErrorListener {
    val TAG = "WanAndroidMAFRequest"

    //配置的第一个域名
    val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    val mMobileRequest: MobileRequest = MobileRequest()

    private var mOkHttpManager: OkHttpManager? = null

    //自定义错误factory的构建
   private val mErrorHandlerFactory: ErrorHandlerFactory = ErrorHandlerFactory.Builder()
        .setBaseUrl(REQUEST_URL_FIRST)
        .addErrorHandler(LoginErrorHandler(),LoginApiException(ERROR_CODE_100.toString()))
        .addErrorHandler(PayErrorHandler(),PayApiException(ERROR_CODE_101.toString()))
        .addErrorHandler(TokenErrorHandler(),TokenApiException(ERROR_CODE_102.toString()))
        .setOnNetWorkErrorListener(this)
        .setOnServiceCodeErrorHandleFinishListener(this)
        .build()

    fun init(application: Application) {
        mOkHttpManager = OkHttpManager.Builder()
            .setApplication(application)
            .setRequestUrl(REQUEST_URL_FIRST)
            .setIsDebug(BuildConfig.DEBUG)
            .setErrorHandlerFactory(mErrorHandlerFactory)
            .setIsShowNetHttpLog(BuildConfig.DEBUG)
            .build()
    }

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        baseServiceDataParseSuFaCall: BaseServiceDataParseSuFaCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = urlMap
            ), baseServiceDataParseSuFaCall, baseServiceDataParseSuFaCall
        )
    }

    /**
     * @date 创建时间: 2023/8/10
     * @auther gaoxiaoxiong
     * @description flow方式请求
     **/
    suspend inline fun <reified T> createRequestFlow(funName: String) = callbackFlow<T> {
        val baseServiceDataParseSuFaCall = object : BaseServiceDataParseSuFaCall<T>() {
            override fun onRequestDataSuccess(data: T?) {
                super.onRequestDataSuccess(data)
                trySend(data!!)
            }
        }
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = mutableMapOf()
            ), baseServiceDataParseSuFaCall, baseServiceDataParseSuFaCall
        )
        awaitClose {}
    }



    private val mJsonParseResult = JsonParseResult()

    /**
     * @date 创建时间: 2023/8/7
     * @auther gxx
     * 自定义api请求的Demo
     **/
    suspend fun <T> readBannerJson(baseServiceDataParseSuFaCall: BaseServiceDataParseSuFaCall<T>) {
        val api = mOkHttpManager?.getApi(REQUEST_URL_FIRST, CustomApiService::class.java)
        val url = "${REQUEST_URL_FIRST}banner/json"
        val responseBody = api?.readBook(url, mutableMapOf())
        mMobileRequest.responseBodyTransformJson(
            REQUEST_URL_FIRST,
            "banner/json",
            responseBody,
            baseServiceDataParseSuFaCall
        ).collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${REQUEST_URL_FIRST}banner/json",
                EmResultType.REQUEST_RESULT_OWN,
                listener = it,
                baseServiceDataParseSuFaCall,
                baseServiceDataParseSuFaCall
            )
        }
    }

    override fun onNetWorkError(throwable: ExceptionHandle.ResponeThrowable) {
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