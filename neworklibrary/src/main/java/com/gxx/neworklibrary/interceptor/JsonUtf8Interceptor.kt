package com.gxx.neworklibrary.interceptor

import com.gxx.neworklibrary.constans.Constant
import okhttp3.Interceptor
import okhttp3.Response

class JsonUtf8Interceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Content-Type", Constant.APPLICATION_JSON_UTF8)
            .build()
        return chain.proceed(newRequest)
    }
}