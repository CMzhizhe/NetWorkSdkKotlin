package com.gxx.networksdkkotlin.network

import com.gxx.networksdkkotlin.network.error.handler.LoginErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.PayErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.TokenErrorHandler
import com.gxx.networksdkkotlin.network.error.handler.UnErrorHandler
import com.gxx.networksdkkotlin.network.factory.FactoryImpl
import com.gxx.networksdkkotlin.network.intercept.InterceptImpl
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
import com.gxx.networksdkkotlin.network.transform.ServiceDataTransform
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.OkHttpRequestManager
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.okbuild.OkBuilder
import com.gxx.neworklibrary.request.MobileRequest

/**
  * @date 创建时间: 2023/7/27
  * @auther gxx
  * @description 用户自定义的 一个网络请求
  **/
object WanAndroidMAFRequest {
    private var mMobileRequest: MobileRequest = MobileRequest(ServiceDataTransform())
    //配置的第一个域名
    private const val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    //自定义错误factory的构建
    val mErrorHandlerFactory = ErrorHandlerFactory()

    init {
        //添加自定义ErrorHandler
        mErrorHandlerFactory
            .addErrorHandler(LoginErrorHandler())
            .addErrorHandler(PayErrorHandler())
            .addErrorHandler(TokenErrorHandler())
            .addErrorHandler(UnErrorHandler())
            .init()
    }

    /**
      * @date 创建时间: 2023/7/27
      * @auther gxx
      * @description 构建 OkBuilder
      **/
    fun createOkBuilder():OkBuilder{
        return OkBuilder()
            .setRequestUrl(REQUEST_URL_FIRST)
            .setIsDebug(BuildConfig.DEBUG)
            .setOnFactoryListener(FactoryImpl())
            .setOnInterceptorListener(InterceptImpl())
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
}