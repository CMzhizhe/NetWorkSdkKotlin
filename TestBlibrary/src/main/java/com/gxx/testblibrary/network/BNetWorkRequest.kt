package com.gxx.testblibrary.network

import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.launreq.AbsLaunchUrlReq
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.okbuild.ParamOkBuilder
import com.gxx.neworklibrary.request.MobileRequest
import com.gxx.testblibrary.network.pase.DataParseSuFaCall
import com.gxx.testblibrary.network.transform.ServiceDataTransform
import retrofit2.Retrofit

object BNetWorkRequest : AbsLaunchUrlReq() {
    //配置的第一个域名
    val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    private val mMobileRequest: MobileRequest = MobileRequest(ServiceDataTransform())



    override fun createParamOkBuilder(): ParamOkBuilder? {
        return ParamOkBuilder()
            .setRequestUrl(REQUEST_URL_FIRST)
            .setIsDebug(BuildConfig.DEBUG)
            .build()
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