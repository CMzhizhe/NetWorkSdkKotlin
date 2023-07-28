package com.gxx.neworklibrary

import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.okbuild.OkBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 管理所有的 Retrofit
 **/
object OkHttpRequestManager {
    private val TAG = "OkHttpRequestManager"
    private var mMapBuilder = hashMapOf<String, OkBuilder>()//构建的 OkBuilder
    private var mMapRetrofit = hashMapOf<String, Retrofit>() // 根据域名构建的 Retrofit


    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 添加 OkBuilder
     **/
    fun addOkBuilder(builder: OkBuilder): OkHttpRequestManager {
        mMapBuilder[builder.getRequestUrl()] = builder
        return this
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 通过okBuilders 创建 Retrofits
     **/
    fun create() {
        if (mMapBuilder.isEmpty()) {
            throw IllegalStateException("未配置任何的 OkBuilder")
        }

        for (baseUrl in mMapBuilder.keys) {
            if (mMapBuilder[baseUrl] == null) {
                continue
            }
            mMapRetrofit[baseUrl] = createRetrofit(mMapBuilder[baseUrl]!!)
        }
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 返回retrofit
     **/
    fun getRetrofit(baseUrl: String): Retrofit? {
        return mMapRetrofit[baseUrl]
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 构建 retrofit
     **/
    fun createRetrofit(builder: OkBuilder): Retrofit {
        val mRequestUrl = builder.getRequestUrl()
        val mRetryOnConnectionFailure = builder.getRetryOnConnectionFailure()
        val mOnInterceptorListener = builder.getOnInterceptorListener()
        val mOnFactoryListener = builder.getOnFactoryListener()

        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(builder.getConnectTimeoutSecond().toLong(), TimeUnit.SECONDS)
            .readTimeout(builder.getReadTimeout().toLong(), TimeUnit.SECONDS)
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
            .baseUrl(mRequestUrl)
            .client(okBuilder.build())

        mOnFactoryListener?.let {
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
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 获取公共API
     * @param url 请求的地址
     **/
    fun <T> getApi(url: String, clazz: Class<T>): T {
        if (url.isEmpty() || mMapRetrofit[url] == null) {
            throw IllegalStateException("当前baseUrl的retrofit没有初始化，请先执行addOkBuilder")
        }
        return mMapRetrofit[url]!!.create(clazz)
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 构建 BaseApiService
     * @param baseUrl 请求的地址
     **/
    @Override
    fun onGetBaseApiService(baseUrl: String): BaseApiService {
        if (mMapRetrofit[baseUrl] == null) {
            throw IllegalStateException("当前baseUrl的retrofit没有初始化，请先执行addOkBuilder")
        }
        return getApi(baseUrl, BaseApiService::class.java)
    }

}