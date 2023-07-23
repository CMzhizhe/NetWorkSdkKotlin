package com.gxx.networksdkkotlin.network

import com.gxx.networksdkkotlin.network.factory.FactoryImpl
import com.gxx.networksdkkotlin.network.intercept.InterceptImpl
import com.gxx.networksdkkotlin.network.transform.ServiceDataTransform
import com.gxx.neworklibrary.OkHttpRequestManager
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.MobileRequest


class MAFRequest {
    private var mOkHttpRequestManager: OkHttpRequestManager? = null
    private var mMobileRequest: MobileRequest? = null

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 初始化
     **/
    fun init() {
        mOkHttpRequestManager = OkHttpRequestManager.Builder()
            .setRequestUrl("https://www.wanandroid.com/")
            .setIsDebug(true)
            .setOnFactoryListener(FactoryImpl())
            .setOnInterceptorListener(InterceptImpl())
            .builder()
        //网络请求构建完成
        mMobileRequest = MobileRequest(mOkHttpRequestManager!!,ServiceDataTransform())
    }

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        method: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        serviceDataParse: ServiceDataParse<T>
    ) {
        mMobileRequest?.get(RqParamModel(method=method,null,urlMap = urlMap),serviceDataParse,serviceDataParse)
    }
}