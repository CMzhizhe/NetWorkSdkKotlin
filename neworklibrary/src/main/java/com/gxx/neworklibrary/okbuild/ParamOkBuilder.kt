package com.gxx.neworklibrary.okbuild


import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener

/**
  * @date 创建时间: 2023/7/27
  * @auther gxx
  * @description ok的Builder
  **/
class ParamOkBuilder {
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeoutSecond = 10//读时间
    private var mWriteTimeOutSecond = 10//写10秒
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = true //默认运行失败重连
    private var mIsDebug = false
    private var mOnFactoryListener: OnFactoryListener? = null //Factory
    private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器


    fun getConnectTimeoutSecond(): Int {
        return mConnectTimeoutSecond
    }

    fun getReadTimeout(): Int {
        return mReadTimeoutSecond
    }

    fun getWriteTimeOut():Int{
        return mWriteTimeOutSecond
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


    fun getOnFactoryListener(): OnFactoryListener? {
        return mOnFactoryListener
    }

    fun getOnInterceptorListener(): OnInterceptorListener? {
        return mOnInterceptorListener
    }


    fun setRequestUrl(url:String):ParamOkBuilder{
        this.mRequestUrl = url
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置intercept
     **/
    fun setOnInterceptorListener(listener: OnInterceptorListener): ParamOkBuilder {
        this.mOnInterceptorListener = listener
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置工厂factory
     **/
    fun setOnFactoryListener(listener: OnFactoryListener): ParamOkBuilder {
        this.mOnFactoryListener = listener
        return this
    }



    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置连接的时间
     **/
    fun setConnectTimeoutSecond(connectionTimeOut: Int): ParamOkBuilder {
        this.mConnectTimeoutSecond = connectionTimeOut
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置读写时间
     **/
    fun setReadTimeout(readTimeout: Int): ParamOkBuilder {
        this.mReadTimeoutSecond = readTimeout
        return this
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/7/29/029
     * @description  写的时间
     **/
    fun setWriteTimeout(writeTimeOut:Int):ParamOkBuilder{
        this.mWriteTimeOutSecond = writeTimeOut
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置是否尝试重连
     **/
    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): ParamOkBuilder {
        this.mRetryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 是否开发者模式
     **/
    fun setIsDebug(isDebug: Boolean): ParamOkBuilder {
        this.mIsDebug = isDebug
        return this
    }



    /**
      * @date 创建时间: 2023/7/27
      * @auther gxx
      * @description 构建
      **/
    fun build():ParamOkBuilder{
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