package com.gxx.networksdkkotlin.network

import android.util.Log
import com.gxx.networksdkkotlin.network.api.CustomApiService
import com.gxx.networksdkkotlin.network.error.handler.LoginErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.PayErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.TokenErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.UnErrorHandler
import com.gxx.networksdkkotlin.network.factory.FactoryImpl
import com.gxx.networksdkkotlin.network.intercept.InterceptImpl
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
import com.gxx.networksdkkotlin.network.transform.ServiceDataTransform
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnBaseApiServiceListener
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 用户自定义的 一个网络请求
 **/
object WanAndroidMAFRequest : OnBaseApiServiceListener {
    val TAG = "WanAndroidMAFRequest"

    //配置的第一个域名
    val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    val mMobileRequest: MobileRequest = MobileRequest(this, ServiceDataTransform())

    val mOkHttpManager: OkHttpManager = OkHttpManager.Builder()
        .setRequestUrl(REQUEST_URL_FIRST)
        .setIsDebug(BuildConfig.DEBUG)
        .setOnFactoryListener(FactoryImpl())
        .setOnInterceptorListener(InterceptImpl())
        .build()

    //自定义错误factory的构建
    val mErrorHandlerFactory = ErrorHandlerFactory()
        .addErrorHandler(LoginErrorHandler())
        .addErrorHandler(PayErrorHandler())
        .addErrorHandler(TokenErrorHandler())
        .addErrorHandler(UnErrorHandler())
        .init()

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        dataParseSuFaCall: DataParseSuFaCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = urlMap
            ), dataParseSuFaCall, dataParseSuFaCall
        )
    }

    suspend inline fun <reified T> createRequestFlow(funName: String) = callbackFlow<T> {
        val dataParseSuFaCall = object : DataParseSuFaCall<T>() {
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
            ), dataParseSuFaCall, dataParseSuFaCall
        )
        awaitClose {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "我被回调了");
            }
        }
    }

    override fun onGetBaseApiService(): BaseApiService? {
        return mOkHttpManager.getApi(REQUEST_URL_FIRST, BaseApiService::class.java)
    }


    private val mJsonParseResult = JsonParseResult()
    /**
     * @date 创建时间: 2023/8/7
     * @auther gxx
     * 自定义api请求的Demo
     **/
    suspend fun <T> readBannerJson(dataParseSuFaCall: DataParseSuFaCall<T>) {
        val api = mOkHttpManager.getApi(REQUEST_URL_FIRST, CustomApiService::class.java)
        val url = "${REQUEST_URL_FIRST}banner/json"
        val responseBody = api?.readBook(url, mutableMapOf())
        mMobileRequest.responseBodyTransformJson(REQUEST_URL_FIRST,"banner/json",responseBody,dataParseSuFaCall).collect{
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${REQUEST_URL_FIRST}banner/json",
                EmResultType.REQUEST_RESULT_OWN,
                listener = it,
                dataParseSuFaCall,
                dataParseSuFaCall
            )
        }
    }
}