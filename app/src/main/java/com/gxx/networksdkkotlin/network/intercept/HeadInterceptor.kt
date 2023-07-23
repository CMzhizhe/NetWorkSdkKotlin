package com.gxx.networksdkkotlin.network.intercept

import okhttp3.Interceptor
import okhttp3.Response

class HeadInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type", "application/json;charset=UTF-8")
        return chain.proceed(builder.build())
    }
}