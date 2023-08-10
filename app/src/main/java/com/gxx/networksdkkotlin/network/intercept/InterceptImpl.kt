package com.gxx.networksdkkotlin.network.intercept

import com.gxx.neworklibrary.inter.OnInterceptorListener
import okhttp3.Interceptor

class InterceptImpl: OnInterceptorListener {
    override fun interceptors(): List<Interceptor> {
        return mutableListOf<Interceptor>();
    }

    override fun netWorkInterceptors(): List<Interceptor> {
        return mutableListOf()
    }
}