package com.gxx.neworklibrary.okbuild


import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import com.gxx.neworklibrary.launreq.AbsLaunchUrlReq

/**
  * @date 创建时间: 2023/7/27
  * @auther gxx
  * @description ok的Builder
  **/
class ReqOkBuilder {
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeout = 10//读写时间
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = false
    private var mIsDebug = false
    private var mOnFactoryListener: OnFactoryListener? = null //Factory
    private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器
    private var mAbsLaunchUrlReq:AbsLaunchUrlReq? = null//配置 请求，域名，错误工厂 的抽象类

    fun getAbsLaunchUrlReq():AbsLaunchUrlReq?{
        return mAbsLaunchUrlReq
    }

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


    fun getOnFactoryListener(): OnFactoryListener? {
        return mOnFactoryListener
    }

    fun getOnInterceptorListener(): OnInterceptorListener? {
        return mOnInterceptorListener
    }


    fun setRequestUrl(url:String):ReqOkBuilder{
        this.mRequestUrl = url
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置intercept
     **/
    fun setOnInterceptorListener(listener: OnInterceptorListener): ReqOkBuilder {
        this.mOnInterceptorListener = listener
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置工厂factory
     **/
    fun setOnFactoryListener(listener: OnFactoryListener): ReqOkBuilder {
        this.mOnFactoryListener = listener
        return this
    }



    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置连接的时间
     **/
    fun setConnectTimeoutSecond(connectionTimeOut: Int): ReqOkBuilder {
        this.mConnectTimeoutSecond = connectionTimeOut
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置读写时间
     **/
    fun setReadTimeout(readTimeout: Int): ReqOkBuilder {
        this.mReadTimeout = readTimeout
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 设置是否尝试重连
     **/
    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): ReqOkBuilder {
        this.mRetryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    /**
     * @date 创建时间: 2023/7/20
     * @auther gaoxiaoxiong
     * @description 是否开发者模式
     **/
    fun setIsDebug(isDebug: Boolean): ReqOkBuilder {
        this.mIsDebug = isDebug
        return this
    }

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 配置 请求，域名，错误工厂 的抽象类
      **/
    fun setAbsLaunchUrlReq(absLaunchUrlReq: AbsLaunchUrlReq): ReqOkBuilder {
        this.mAbsLaunchUrlReq = absLaunchUrlReq
        return this
    }

    /**
      * @date 创建时间: 2023/7/27
      * @auther gxx
      * @description 构建
      **/
    fun build():ReqOkBuilder{
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