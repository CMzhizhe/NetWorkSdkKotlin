package com.gxx.neworklibrary

import android.app.Application
import android.content.Context
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnCommonParamsListener
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
    private var mCacheOnCommonParams = mutableMapOf<String, OnCommonParamsListener>()//公共参数

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
     * @param keyName 字段名称
     */
    fun putConfig(keyName: String, httpConfigModel: HttpConfigModel) {
        mCatchHttpConfigMap[keyName] = httpConfigModel
    }

    /**
     * @author gaoxiaoxiong
     * 获取 HttpConfigModel
     * @param keyName 字段名称
     */
    fun getConfig(keyName: String):HttpConfigModel?{
      return mCatchHttpConfigMap[keyName]
    }

    fun getContext(): Context {
        return mContext
    }

    class Builder {
        var mIsShowNetHttpLog = false//是否打印网络日志
        var mOnFactoryListener: OnFactoryListener? = null //Factory
        var mOnInterceptorListener: OnInterceptorListener? = null // 拦截器
        var mErrorHandlerFactory: ErrorHandlerFactory? = null//错误Handler
        var mOnCommonParamsListener:OnCommonParamsListener? = null;//公共参数
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
            .connectTimeout(httpConfigModel.connectTime.toLong(), TimeUnit.SECONDS)
            .readTimeout(httpConfigModel.readTime.toLong(), TimeUnit.SECONDS)
            .writeTimeout(httpConfigModel.writeTime.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(httpConfigModel.retryOnConnection)

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
            .baseUrl(httpConfigModel.hostUrl)
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

        builder.mErrorHandlerFactory?.let {
            putErrorHandlerFactory(httpConfigModel.hostUrl,it)
        }

        builder.mOnCommonParamsListener?.let {
            putCommonParamsListener(httpConfigModel.hostUrl,it);
        }

        return reBuilder.build()
    }

    /**
      * 设置公共参数的接口
      */
    fun putCommonParamsListener(baseUrl: String,commonParamsListener: OnCommonParamsListener){
        mCacheOnCommonParams[baseUrl] = commonParamsListener;
    }

    /**
     * @author gaoxiaoxiong
     * 存放 ErrorHandlerFactory
     */
    fun putErrorHandlerFactory(baseUrl: String,errorHandlerFactory: ErrorHandlerFactory){
        mCacheErrorHandler[baseUrl] = errorHandlerFactory
    }

    fun getErrorHandlerFactory(baseUrl: String): ErrorHandlerFactory? {
        return mCacheErrorHandler[baseUrl]
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 返回retrofit
     * @param hostUrl
     **/
    fun getRetrofit(hostUrl: String): Retrofit? {
        return mCatchMapRetrofit[hostUrl]
    }

    /**
     * @author gaoxiaoxiong
     * put Retrofit
     * @param hostUrl 域名
     */
    fun putRetrofit(hostUrl:String,retrofit:Retrofit){
        mCatchMapRetrofit[hostUrl] = retrofit
    }

    /**
      * @param hostUrl 域名
      */
    fun getOnCommonParamsListener(hostUrl:String):OnCommonParamsListener?{
       return mCacheOnCommonParams[hostUrl]
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 获取公共API
     **/
    fun <T> getApi(hostUrl: String, clazz: Class<T>): T? {
        return getRetrofit(hostUrl)?.create(clazz)
    }


}