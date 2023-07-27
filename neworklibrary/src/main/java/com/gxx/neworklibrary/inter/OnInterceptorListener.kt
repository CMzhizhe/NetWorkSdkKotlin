package com.gxx.neworklibrary.inter

import okhttp3.Interceptor

interface OnInterceptorListener {
    fun interceptors(): List<Interceptor>

    fun netWorkInterceptors(): List<Interceptor>
}