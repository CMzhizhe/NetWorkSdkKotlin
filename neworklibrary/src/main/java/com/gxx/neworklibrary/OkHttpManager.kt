package com.gxx.neworklibrary

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnFactoryListener
import com.gxx.neworklibrary.inter.OnInterceptorListener
import com.gxx.neworklibrary.interceptor.JsonUtf8Interceptor
import com.gxx.neworklibrary.model.HttpConfigModel
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
    private lateinit var mApplication: Application
    private lateinit var mContext: Context
    private val mCatchHttpConfigMap = mutableMapOf<String, HttpConfigModel>()//存储配置信息，key为hostUrl
    private val mCatchMapRetrofit = mutableMapOf<String, Retrofit>()//存储OkHttpManager，key为hostUrl
    private val mCacheErrorHandler = mutableMapOf<String, ErrorHandlerFactory>()

    companion object {
        private var INSTANCE: OkHttpManager? = null
        fun getInstance(): OkHttpManager {
            if (INSTANCE == null) {
                synchronized(OkHttpManager::class) {
                    if (INSTANCE == null) {
                        INSTANCE = OkHttpManager()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    /**
     * @author gaoxiaoxiong
     * 与application进行绑定
     */
    fun bindApplication(application: Application): OkHttpManager {
        this.mApplication = application
        this.mContext = application
        return this
    }

    /**
     * @author gaoxiaoxiong
     * 配置 context
     */
    fun init(context: Context) {
        this.mContext = context
    }

    /**
     * @author gaoxiaoxiong
     * 放置配置的clss
     * @param hostUrl
     */
    fun putConfig(hostUrl: String, httpConfigModel: HttpConfigModel) {
        mCatchHttpConfigMap[hostUrl] = httpConfigModel
    }

    fun getContext(): Context {
        return mContext
    }

    class Builder {
        var mIsShowNetHttpLog = false//是否打印网络日志
        var mOnFactoryListener: OnFactoryListener? = null //Factory
        var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器
        var mErrorHandlerFactory: ErrorHandlerFactory? = null//错误Handler
    }

    /**
     * @date 创建时间: 2023/7/31
     * @auther gaoxiaoxiong
     * @description 初始化
     * @param httpConfigModel 域名控制的model
     * @param builder
     **/
    fun createRetrofit(httpConfigModel: HttpConfigModel,builder: Builder): Retrofit {
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(httpConfigModel.model.connectTime.toLong(), TimeUnit.SECONDS)
            .readTimeout(httpConfigModel.model.readTime.toLong(), TimeUnit.SECONDS)
            .writeTimeout(httpConfigModel.model.writeTime.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(httpConfigModel.model.retryOnConnection)

        okBuilder.apply {
            //添加 application/json; charset=UTF-8
            addInterceptor(JsonUtf8Interceptor())
            //打印日志
            if (builder.mIsShowNetHttpLog) {
                addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            //添加拦截器
            builder.mOnInterceptorListener?.let {
                for (interceptor in it.interceptors()) {
                    addInterceptor(interceptor)
                }

                for (netWorkInterceptor in it.netWorkInterceptors()) {
                    addNetworkInterceptor(netWorkInterceptor)
                }
            }
        }

        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(httpConfigModel.model.hostUrl)
            .client(okBuilder.build())

        reBuilder.addConverterFactory(GsonConverterFactory.create())
        reBuilder.addCallAdapterFactory(FlowCallAdapterFactory())

        //可以添加其他的CallAdapterFactory、ConverterFactory
        builder.mOnFactoryListener?.let {
            for (callAdapterFactory in it.callAdapterFactorys()) {
                reBuilder.addCallAdapterFactory(callAdapterFactory)
            }

            for (converterFactory in it.converterFactorys()) {
                reBuilder.addConverterFactory(converterFactory)
            }
        }
        return reBuilder.build()
    }

    fun getErrorHandlerFactory(baseUrl: String): ErrorHandlerFactory? {
        return mCacheErrorHandler[baseUrl]
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 返回retrofit
     * @param baseUrl
     **/
    fun getRetrofit(baseUrl: String): Retrofit? {
        return mCatchMapRetrofit[baseUrl]
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 获取公共API
     **/
    fun <T> getApi(baseUrl: String, clazz: Class<T>): T? {
        return getRetrofit(baseUrl)?.create(clazz)
    }


}