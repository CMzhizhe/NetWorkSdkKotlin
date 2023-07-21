package com.gxx.neworklibrary

import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class OkHttpRequestManager {
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeout = 10//读写时间
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = false
    private var mIsDevmodel = false
    private var mExceptions = mutableListOf<AbsApiException>()
    private var mOnFactoryListener: OnFactoryListener? = null
    private var mOnInterceptorListener: OnInterceptorListener? = null
    private var mRetrofit:Retrofit?=null

    constructor(builder: Builder) {
        this.mConnectTimeoutSecond = builder.getConnectTimeoutSecond()
        this.mExceptions = builder.getExceptions()
        this.mRequestUrl = builder.getRequestUrl()
        this.mReadTimeout = builder.getReadTimeout()
        this.mIsDevmodel = builder.getIsDevmodel()
        this.mRetryOnConnectionFailure = builder.getRetryOnConnectionFailure()

        val logInterceptor = HttpLoggingInterceptor() //日志太多会崩溃
        if (mIsDevmodel) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(mConnectTimeoutSecond.toLong(), TimeUnit.SECONDS)
            .readTimeout(mReadTimeout.toLong(), TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(mRetryOnConnectionFailure) //是否失败重新请求连接

        if (mOnInterceptorListener != null) {
            for (interceptor in mOnInterceptorListener!!.interceptors()) {
                okBuilder.addInterceptor(interceptor)
            }

            for (netWorkInterceptor in mOnInterceptorListener!!.netWorkInterceptors()) {
                okBuilder.addNetworkInterceptor(netWorkInterceptor)
            }
        }

        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(this.mRequestUrl)
            .client(okBuilder.build())

        if (mOnFactoryListener != null) {
            for (callAdapterFactory in mOnFactoryListener!!.callAdapterFactorys()) {
                reBuilder.addCallAdapterFactory(callAdapterFactory)
            }

            for (converterFactory in mOnFactoryListener!!.converterFactorys()) {
                reBuilder.addConverterFactory(converterFactory)
            }
        }

        mRetrofit = reBuilder.build()
    }




    /**
      * @date 创建时间: 2023/7/21
      * @auther gxx
      * @description 构建 BaseApiService
      **/
    fun createBaseApi():BaseApiService{
        return getApi(BaseApiService::class.java)
    }

    /**
      * @date 创建时间: 2023/7/21
      * @auther gxx
      * @description 获取公共API
      **/
    fun <T> getApi(clazz: Class<T>): T {
        return mRetrofit!!.create(clazz)
    }

    class Builder {
        private var mConnectTimeoutSecond = 10//连接时间
        private var mReadTimeout = 10//读写时间
        private var mRequestUrl: String = ""//连接地址
        private var mRetryOnConnectionFailure = false
        private var mIsDevmodel = false
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

        fun getIsDevmodel(): Boolean {
            return mIsDevmodel
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
         * @description 设置 AbsApiException
         **/
        fun setApiExceptions(exceptions: MutableList<AbsApiException>) : Builder{
            this.mExceptions = exceptions
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
            this.mReadTimeout = readTimeout
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
        fun setIsDevmodel(isDevmodel: Boolean): Builder {
            this.mIsDevmodel = isDevmodel
            return this
        }
    }
}