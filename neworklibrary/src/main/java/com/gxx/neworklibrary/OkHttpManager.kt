package com.gxx.neworklibrary

import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.launreq.AbsLaunchUrlReq
import com.gxx.neworklibrary.okbuild.ParamOkBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 管理所有的 Retrofit
 **/
object OkHttpManager {
    private val TAG = "OkHttpRequestManager"
    private var mMapParamOkBuilder = hashMapOf<String, ParamOkBuilder>()//构建的 OkBuilder
    private var mMapRetrofit = hashMapOf<String, Retrofit>() // 根据域名构建的 Retrofit

    /**
     * @date 创建时间: 2023/7/28
     * @auther gxx
     * @description 添加 AbsLaunchUrlReq
     **/
    fun addAbsLaunchUrlReq(absLaunchUrlReq: AbsLaunchUrlReq):OkHttpManager {
        if (absLaunchUrlReq.baseUrl().isEmpty()){
            throw IllegalStateException("baseUrl 是空的")
        }
        if (absLaunchUrlReq.createRetrofit2() == null && absLaunchUrlReq.createParamOkBuilder() == null) {
            throw IllegalStateException("不能2个同时为空")
        }

        if (absLaunchUrlReq.createRetrofit2() != null) {
            mMapRetrofit[absLaunchUrlReq.baseUrl()] = absLaunchUrlReq.createRetrofit2()!!
        }else if (absLaunchUrlReq.createParamOkBuilder()!=null){
            mMapParamOkBuilder[absLaunchUrlReq.baseUrl()] = absLaunchUrlReq.createParamOkBuilder()!!
        }else{//2个都不是空的，以createPamOkBuilder为准
            mMapParamOkBuilder[absLaunchUrlReq.baseUrl()] = absLaunchUrlReq.createParamOkBuilder()!!
        }
        return this
    }

    /**
     * @date 创建时间: 2023/7/27
     * @auther gxx
     * @description 通过okBuilders 创建 Retrofits
     **/
    fun create() {
        for (baseUrl in mMapParamOkBuilder.keys) {
            if (mMapParamOkBuilder[baseUrl] == null) {
                continue
            }
            if (mMapRetrofit[baseUrl] != null) {
                continue
            }
            mMapRetrofit[baseUrl] = createRetrofit(mMapParamOkBuilder[baseUrl]!!)
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
    private fun createRetrofit(builder: ParamOkBuilder): Retrofit {
        val mRequestUrl = builder.getRequestUrl()
        val mRetryOnConnectionFailure = builder.getRetryOnConnectionFailure()
        val mOnInterceptorListener = builder.getOnInterceptorListener()
        val mOnFactoryListener = builder.getOnFactoryListener()

        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(builder.getConnectTimeoutSecond().toLong(), TimeUnit.SECONDS)
            .readTimeout(builder.getReadTimeout().toLong(), TimeUnit.SECONDS)
            .writeTimeout(builder.getWriteTimeOut().toLong(),TimeUnit.SECONDS)
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
    fun getBaseApiService(baseUrl: String): BaseApiService {
        if (mMapRetrofit[baseUrl] == null) {
            throw IllegalStateException("当前baseUrl的retrofit没有初始化，请先执行addOkBuilder")
        }
        return getApi(baseUrl, BaseApiService::class.java)
    }

}