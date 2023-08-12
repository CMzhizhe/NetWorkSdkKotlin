package com.gxx.neworklibrary

import android.app.Application
import android.net.Uri
import android.util.Log
import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import com.gxx.neworklibrary.interceptor.JsonUtf8Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.thdev.network.flowcalladapterfactory.FlowCallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 管理所有的 Retrofit
 **/
class OkHttpManager {
    private val TAG = "OkHttpManager"

    companion object{
        private lateinit var mApplication: Application
        private val mCatchMapRetrofit = mutableMapOf<String, Retrofit>()//存储OkHttpManager，key为baseUrl
        private val mCacheErrorHandler = mutableMapOf<String, ErrorHandlerFactory>()
        private val mObj = Any()

        fun getApplication():Application{
            return mApplication
        }

        fun getRetrofit(baseUrl: String):Retrofit?{
            return mCatchMapRetrofit[baseUrl]
        }

        fun getErrorHandlerFactory(baseUrl: String):ErrorHandlerFactory?{
            return mCacheErrorHandler[baseUrl]
        }
    }

    private constructor(builder: Builder) {
        synchronized(mObj){
            val startTime = System.currentTimeMillis()
            mApplication = builder.getApplication()!!

            mCatchMapRetrofit.getOrPut(builder.getRequestUrl()){
                createRetrofit(builder)
            }

            if (builder.getErrorHandlerFactory()!=null){
                mCacheErrorHandler.getOrPut(builder.getRequestUrl()){
                    builder.getErrorHandlerFactory()!!
                }
            }

           Log.d(TAG,"创建耗时-->${System.currentTimeMillis() - startTime}")
        }
    }

    class Builder {
        private var mApplication:Application?=null
        private var mConnectTimeoutSecond = 15//连接时间
        private var mReadTimeoutSecond = 30//读时间
        private var mWriteTimeOutSecond = 30//写10秒
        private var mRequestUrl: String = ""//连接地址
        private var mRetryOnConnectionFailure = true //默认运行失败重连
        private var mErrorHandlerFactory:ErrorHandlerFactory?=null//错误Handler
        private var mIsDebug = false
        private var mIsShowNetHttpLog = false//是否打印网络日志
        private var mIsContentTypeApplicationUtf8 = true//是否默认   application/json; charset=UTF-8
        private var mOnFactoryListener: OnFactoryListener? = null //Factory
        private var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器

        fun getErrorHandlerFactory():ErrorHandlerFactory?{
            return mErrorHandlerFactory
        }

        fun getApplication():Application?{
            return mApplication
        }

        fun getIsShowNetHttpLog():Boolean{
            return mIsShowNetHttpLog
        }

        fun getIsContentTypeApplicationUtf8():Boolean{
            return mIsContentTypeApplicationUtf8
        }

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

        fun setApplication(application: Application): Builder {
            this.mApplication = application
            return this
        }

        fun setErrorHandlerFactory(factory: ErrorHandlerFactory):Builder{
            this.mErrorHandlerFactory = factory
            return this
        }

        fun setRequestUrl(url: String): Builder {
            this.mRequestUrl = url
            return this
        }

        fun setIsShowNetHttpLog(boolean: Boolean): Builder {
            this.mIsShowNetHttpLog = boolean
            return this
        }

        fun setIsContentTypeApplicationUtf8(boolean: Boolean): Builder{
            this.mIsContentTypeApplicationUtf8 = boolean
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

            if (mApplication == null){
                throw IllegalStateException("mApplication 为空")
            }

            val uri = Uri.parse(mRequestUrl)
            if (uri.port == Constant.DEFAULT_PORT_80 || uri.port == Constant.DEFAULT_PORT_443) {
                throw IllegalStateException("默认端口号，不用去加上")
            }

            return OkHttpManager(this)
        }
    }

    /**
     * @date 创建时间: 2023/7/31
     * @auther gaoxiaoxiong
     * @description 初始化
     **/
    private fun createRetrofit(builder: Builder):Retrofit {
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(builder.getConnectTimeoutSecond().toLong(), TimeUnit.SECONDS)
            .readTimeout(builder.getReadTimeout().toLong(), TimeUnit.SECONDS)
            .writeTimeout(builder.getWriteTimeOut().toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(builder.getRetryOnConnectionFailure()) //是否失败重新请求连接


        if (builder.getIsShowNetHttpLog() && builder.getIsDebug()){
            okBuilder.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        if (builder.getIsContentTypeApplicationUtf8()){
            okBuilder.addInterceptor(JsonUtf8Interceptor())
        }

        builder.getOnInterceptorListener()?.let {
            for (interceptor in it.interceptors()) {
                okBuilder.addInterceptor(interceptor)
            }

            for (netWorkInterceptor in it.netWorkInterceptors()) {
                okBuilder.addNetworkInterceptor(netWorkInterceptor)
            }
        }

        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(builder.getRequestUrl())
            .client(okBuilder.build())

        reBuilder.addConverterFactory(GsonConverterFactory.create())
        reBuilder.addCallAdapterFactory(FlowCallAdapterFactory())

        //可以添加其他的CallAdapterFactory、ConverterFactory
        builder.getOnFactoryListener()?.let {
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
     * @param baseUrl
     **/
    fun getRetrofit(baseUrl: String): Retrofit {
        if (mCatchMapRetrofit[baseUrl] == null) {
            throw IllegalStateException("retrofit == null 请先builder")
        }
        return mCatchMapRetrofit[baseUrl]!!
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 获取公共API
     **/
    fun <T> getApi(baseUrl: String, clazz: Class<T>): T? {
        if (mCatchMapRetrofit[baseUrl] == null) {
            throw IllegalStateException("请先builder")
        }
        return getRetrofit(baseUrl).create(clazz)
    }


}