package com.gxx.networksdkkotlin.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
  * @date 创建时间: 2023/8/7
  * @auther gxx
  * 自定义api
  **/
interface CustomApiService {

    /**
      * @date 创建时间: 2023/8/7
      * @auther gxx
      * 自定义接口名称
      **/
    @JvmSuppressWildcards
    @GET
    suspend fun readBook(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody
}