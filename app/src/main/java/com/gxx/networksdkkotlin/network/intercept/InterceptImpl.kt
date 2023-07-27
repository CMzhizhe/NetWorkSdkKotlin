package com.gxx.networksdkkotlin.network.intercept

import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.neworklibrary.inter.OnInterceptorListener
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class InterceptImpl: OnInterceptorListener {
    override fun interceptors(): List<Interceptor> {
        val list = mutableListOf<Interceptor>()
        list.add(HeadInterceptor())
        val interceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
           interceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            interceptor.level =HttpLoggingInterceptor.Level.NONE
        }
        list.add(interceptor)
        return list;
    }

    override fun netWorkInterceptors(): List<Interceptor> {
        return mutableListOf()
    }
}