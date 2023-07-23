package com.gxx.neworklibrary

import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import com.gxx.neworklibrary.inter.OnOkHttpRequestManagerListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.neworklibrary.resultcall.AbsRequestResultImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class OkHttpRequestManager : OnOkHttpRequestManagerListener {
    private var mConnectTimeoutSecond = 10//连接时间
    private var mReadTimeout = 10//读写时间
    private var mRequestUrl: String = ""//连接地址
    private var mRetryOnConnectionFailure = false
    private var mIsDebug = false
    private var mExceptions = mutableListOf<AbsApiException>()
    private var mOnFactoryListener: OnFactoryListener? = null
    private var mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener? = null
    private var mOnInterceptorListener: OnInterceptorListener? = null
    private var mRetrofit:Retrofit?=null
    private var mMobileRequest = MobileRequest()


    private constructor(builder: Builder) {
        //做检查操作
        if (builder.getRequestUrl().isEmpty()){
            throw IllegalStateException("请求地址是空的")
        }

        if (builder.getOnResponseBodyTransformJsonListener() == null){
            throw IllegalStateException("BaseBean 解析器为设置")
        }


        this.mConnectTimeoutSecond = builder.getConnectTimeoutSecond()
        this.mExceptions = builder.getExceptions()
        this.mRequestUrl = builder.getRequestUrl()
        this.mReadTimeout = builder.getReadTimeout()
        this.mIsDebug = builder.getIsDebug()
        this.mRetryOnConnectionFailure = builder.getRetryOnConnectionFailure()
        this.mOnResponseBodyTransformJsonListener = builder.getOnResponseBodyTransformJsonListener()
        this.mOnInterceptorListener = builder.getOnInterceptorListener()
        this.mOnFactoryListener = builder.getOnFactoryListener()

        mMobileRequest.setOnResponseBodyTransformJsonListener(mOnResponseBodyTransformJsonListener!!)


        val logInterceptor = HttpLoggingInterceptor()
        if (mIsDebug) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(mConnectTimeoutSecond.toLong(), TimeUnit.SECONDS)
            .readTimeout(mReadTimeout.toLong(), TimeUnit.SECONDS)
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(mRetryOnConnectionFailure) //是否失败重新请求连接

        mOnInterceptorListener?.let {
            for (interceptor in it.interceptors()) {
                okBuilder.addInterceptor(interceptor)
            }

            for (netWorkInterceptor in it.netWorkInterceptors()) {
                okBuilder.addNetworkInterceptor(netWorkInterceptor)
            }
        }


        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(this.mRequestUrl)
            .client(okBuilder.build())


        mOnFactoryListener?.let {
            for (callAdapterFactory in it.callAdapterFactorys()) {
                reBuilder.addCallAdapterFactory(callAdapterFactory)
            }

            for (converterFactory in it.converterFactorys()) {
                reBuilder.addConverterFactory(converterFactory)
            }
        }


        mRetrofit = reBuilder.build()

        mMobileRequest.setOnOkHttpRequestManagerListener(this)

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
        if (mRetrofit == null){
            throw IllegalStateException("请先builder")
        }
        return mRetrofit!!.create(clazz)
    }

    class Builder {
        private var mConnectTimeoutSecond = 10//连接时间
        private var mReadTimeout = 10//读写时间
        private var mRequestUrl: String = ""//连接地址
        private var mRetryOnConnectionFailure = false
        private var mIsDebug = false
        private var mExceptions = mutableListOf<AbsApiException>()
        private var mOnFactoryListener: OnFactoryListener? = null //Factory
        private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器
        private var mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener? = null//BaseBean 解析器


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

        fun getOnResponseBodyTransformJsonListener():OnResponseBodyTransformJsonListener?{
            return mOnResponseBodyTransformJsonListener
        }

        fun setRequestUrl(url:String):Builder{
            this.mRequestUrl = url
            return this
        }

        fun setOnResponseBodyTransformJsonListener(listener:OnResponseBodyTransformJsonListener):Builder{
            this.mOnResponseBodyTransformJsonListener = listener
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
        fun setIsDebug(isDebug: Boolean): Builder {
            this.mIsDebug = isDebug
            return this
        }

        fun builder():OkHttpRequestManager{
            return OkHttpRequestManager(this)
        }
    }

    override fun onGetOkHttpRequestManager(): OkHttpRequestManager {
        return this
    }

    override fun onApiExceptions(): MutableList<AbsApiException> {
       return mExceptions
    }

    fun getMobileRequest():MobileRequest{
        return mMobileRequest
    }
}