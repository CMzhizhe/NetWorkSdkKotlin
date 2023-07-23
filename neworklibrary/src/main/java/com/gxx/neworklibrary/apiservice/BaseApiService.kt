package com.gxx.neworklibrary.apiservice

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 基类Base配置
 **/
interface BaseApiService {
    @JvmSuppressWildcards
    @GET
    suspend fun getJson(
        @Url url: String,
        @QueryMap map: Map<String, Any>
    ): ResponseBody

}