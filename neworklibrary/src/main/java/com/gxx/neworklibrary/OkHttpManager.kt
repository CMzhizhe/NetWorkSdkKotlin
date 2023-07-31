package com.gxx.neworklibrary

import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 管理所有的 Retrofit
 **/
class OkHttpManager {
    private val TAG = "OkHttpRequestManager"
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeoutSecond = 30//读时间
    private var mWriteTimeOutSecond = 30//写10秒
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = true //默认运行失败重连
    private var mIsDebug = false
    private var mOnFactoryListener: OnFactoryListener? = null //Factory
    private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器
    private var mRetrofit:Retrofit


    private constructor(builder: Builder) {
        this.mConnectTimeoutSecond = builder.getConnectTimeoutSecond()
        this.mReadTimeoutSecond = builder.getReadTimeout()
        this.mWriteTimeOutSecond = builder.getWriteTimeOut()
        this.mRequestUrl = builder.getRequestUrl()
        this.mRetryOnConnectionFailure = builder.getRetryOnConnectionFailure()
        this.mIsDebug = builder.getIsDebug()
        this.mOnFactoryListener = builder.getOnFactoryListener()
        this.mOnInterceptorListener = builder.getOnInterceptorListener()
        mRetrofit = init()
    }

    class Builder {
        private var mConnectTimeoutSecond = 10//连接时间
        private var mReadTimeoutSecond = 30//读时间
        private var mWriteTimeOutSecond = 30//写10秒
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

        fun getWriteTimeOut(): Int {
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


        fun setRequestUrl(url: String): Builder {
            this.mRequestUrl = url
            return this
        }

        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 设置intercept
         **/
        fun setOnInterceptorListener(listener: OnInterceptorListener): Builder {
            this.mOnInterceptorListener = listener
            return this
        }

        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 设置工厂factory
         **/
        fun setOnFactoryListener(listener: OnFactoryListener): Builder {
            this.mOnFactoryListener = listener
            return this
        }


        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 设置连接的时间
         **/
        fun setConnectTimeoutSecond(connectionTimeOut: Int): Builder {
            this.mConnectTimeoutSecond = connectionTimeOut
            return this
        }

        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 设置读写时间
         **/
        fun setReadTimeout(readTimeout: Int): Builder {
            this.mReadTimeoutSecond = readTimeout
            return this
        }

        /**
         * @author gaoxiaoxiong
         * @date 创建时间: 2023/7/29/029
         * @description  写的时间
         **/
        fun setWriteTimeout(writeTimeOut: Int): Builder {
            this.mWriteTimeOutSecond = writeTimeOut
            return this
        }

        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 设置是否尝试重连
         **/
        fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): Builder {
            this.mRetryOnConnectionFailure = retryOnConnectionFailure
            return this
        }

        /**
         * @date 创建时间: 2023/7/20
         * @auther gaoxiaoxiong
         * @description 是否开发者模式
         **/
        fun setIsDebug(isDebug: Boolean): Builder {
            this.mIsDebug = isDebug
            return this
        }

        /**
         * @date 创建时间: 2023/7/27
         * @auther gxx
         * @description 构建
         **/
        fun build(): OkHttpManager {
            if (mRequestUrl.isEmpty()) {
                throw IllegalStateException("RequestUrl is empty")
            }

            //判断mRequestUrl 是否 /结尾
            if (mRequestUrl.last().toString() != "/") {
                throw IllegalStateException("RequestUrl is 需要以 '/' 结尾，形如www.xxx.com/")
            }

            return OkHttpManager(this)
        }
    }

    /**
     * @date 创建时间: 2023/7/31
     * @auther gaoxiaoxiong
     * @description 初始化
     **/
    private fun init():Retrofit {
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(this.mConnectTimeoutSecond.toLong(), TimeUnit.SECONDS)
            .readTimeout(this.mReadTimeoutSecond.toLong(), TimeUnit.SECONDS)
            .writeTimeout(this.mWriteTimeOutSecond.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(this.mRetryOnConnectionFailure) //是否失败重新请求连接

        this.mOnInterceptorListener?.let {
            for (interceptor in it.interceptors()) {
                okBuilder.addInterceptor(interceptor)
            }

            for (netWorkInterceptor in it.netWorkInterceptors()) {
                okBuilder.addNetworkInterceptor(netWorkInterceptor)
            }
        }

        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(mRequestUrl)
            .client(okBuilder.build())

        this.mOnFactoryListener?.let {
            for (callAdapterFactory in it.callAdapterFactorys()) {
                reBuilder.addCallAdapterFactory(callAdapterFactory)
            }

            for (converterFactory in it.converterFactorys()) {
                reBuilder.addConverterFactory(converterFactory)
            }
        }

       return reBuilder.build()
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 返回retrofit
     **/
    fun getRetrofit(): Retrofit {
        return mRetrofit
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 获取公共API
     **/
    fun <T> getApi(clazz: Class<T>): T {
        return getRetrofit().create(clazz)
    }




}