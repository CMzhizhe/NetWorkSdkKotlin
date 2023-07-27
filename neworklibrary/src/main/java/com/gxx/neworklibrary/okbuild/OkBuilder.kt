package com.gxx.neworklibrary.okbuild


import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener

/**
  * @date 创建时间: 2023/7/27
  * @auther gxx
  * @description ok的Builder
  **/
class OkBuilder {
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeout = 10//读写时间
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = false
    private var mIsDebug = false
    private var mExceptions = mutableListOf<AbsApiException>()
    private var mOnFactoryListener: OnFactoryListener? = null //Factory
    private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器


    fun getConnectTimeoutSecond(): Int {
        return mConnectTimeoutSecond
    }

    fun getReadTimeout(): Int {
        return mReadTimeout
    }

    fun getRequestUrl(): String {
        return mRequestUrl
    }

    fun getRetryOnConnectionFailure(): Boolean {
        return mRetryOnConnectionFailure
    }

    fun getIsDebug(): Boolean {
        return mIsDebug
    }

    fun getExceptions(): MutableList<AbsApiException> {
        return mExceptions
    }

    fun getOnFactoryListener(): OnFactoryListener? {
        return mOnFactoryListener
    }

    fun getOnInterceptorListener(): OnInterceptorListener? {
        return mOnInterceptorListener
    }


    fun setRequestUrl(url:String):OkBuilder{
        this.mRequestUrl = url
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置intercept
     **/
    fun setOnInterceptorListener(listener: OnInterceptorListener): OkBuilder {
        this.mOnInterceptorListener = listener
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置工厂factory
     **/
    fun setOnFactoryListener(listener: OnFactoryListener): OkBuilder {
        this.mOnFactoryListener = listener
        return this
    }



    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置连接的时间
     **/
    fun setConnectTimeoutSecond(connectionTimeOut: Int): OkBuilder {
        this.mConnectTimeoutSecond = connectionTimeOut
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置读写时间
     **/
    fun setReadTimeout(readTimeout: Int): OkBuilder {
        this.mReadTimeout = readTimeout
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置是否尝试重连
     **/
    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): OkBuilder {
        this.mRetryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 是否开发者模式
     **/
    fun setIsDebug(isDebug: Boolean): OkBuilder {
        this.mIsDebug = isDebug
        return this
    }

    /**
      * @date 创建时间: 2023/7/27
      * @auther gxx
      * @description 构建
      **/
    fun build():OkBuilder{
        if (mRequestUrl.isEmpty()){
            throw IllegalStateException("RequestUrl is empty")
        }

        //判断mRequestUrl 是否 /结尾
        if (mRequestUrl.last().toString()!="/"){
            throw IllegalStateException("RequestUrl is 需要以 '/' 结尾，形如www.xxx.com/")
        }

        return this
    }
}