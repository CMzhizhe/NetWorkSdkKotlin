package com.gxx.neworklibrary.inter

import okhttp3.Interceptor

interface OnOkHttpInterceptorListener {

    /**
      * 普通类型的拦截器
      */
    fun normalInterceptors(): List<Interceptor>

    /**
      * 专门用于网络的拦截器
      */
    fun netWorkInterceptors(): List<Interceptor>
}