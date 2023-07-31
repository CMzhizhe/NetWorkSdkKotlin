package com.gxx.testalibrary.network

import com.gxx.neworklibrary.launreq.AbsLaunchUrlReq
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.okbuild.ParamOkBuilder
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.testalibrary.network.pase.DataParseSuFaCall
import com.gxx.testalibrary.network.transform.ServiceDataTransform
import retrofit2.Retrofit

/**
 * @date 创建时间: 2023/7/31
 * @auther gxx
 * @description
 *  这里故意配置跟主APP相同的域名，主要是测试 是否会使用统一个retrofit2。提供给主APP调用，这里提供的baseURl或MobileRequest，都可以由外部进行提供
 **/
object ANetWorkRequest : AbsLaunchUrlReq() {
    //配置的第一个域名
    val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    private val mMobileRequest: MobileRequest = MobileRequest(ServiceDataTransform())
    private var mRetrofit:Retrofit? = null

    /**
      * @date 创建时间: 2023/7/31
      * @auther gxx
      * @description  设置mRetrofit
      **/
    fun setRetrofit(retrofit: Retrofit):ANetWorkRequest{
        this.mRetrofit = retrofit
        return this
    }

    override fun createParamOkBuilder(): ParamOkBuilder? {
        return null
    }

    override fun createRetrofit2(): Retrofit? {
        return mRetrofit
    }

    override fun baseUrl(): String {
        return REQUEST_URL_FIRST
    }


    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        dataParseSuFaCall: DataParseSuFaCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = urlMap
            ), dataParseSuFaCall, dataParseSuFaCall
        )
    }

}