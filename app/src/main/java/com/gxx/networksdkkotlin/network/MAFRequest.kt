package com.gxx.networksdkkotlin.network

import com.gxx.networksdkkotlin.network.factory.FactoryImpl
import com.gxx.networksdkkotlin.network.intercept.InterceptImpl
import com.gxx.networksdkkotlin.network.transform.ServiceDataTransform
import com.gxx.neworklibrary.OkHttpRequestManager


class MAFRequest {
    private var mOkHttpRequestManager: OkHttpRequestManager? = null

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 初始化
     **/
    fun init() {
        mOkHttpRequestManager = OkHttpRequestManager.Builder()
            .setRequestUrl("https://www.wanandroid.com/")
            .setOnResponseBodyTransformJsonListener(ServiceDataTransform())
            .setIsDebug(true)
            .setOnFactoryListener(FactoryImpl())
            .setOnInterceptorListener(InterceptImpl())
            .builder()
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
        mOkHttpRequestManager!!.getMobileRequest()
            .doGetRequest(method, urlMap, false, 1, serviceDataParse, serviceDataParse)
    }
}