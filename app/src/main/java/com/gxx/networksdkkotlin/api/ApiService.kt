package com.gxx.networksdkkotlin.api

import com.gxx.networksdkkotlin.bean.Response
import com.gxx.neworklibrary.bean.Banner
import retrofit2.http.GET

interface ApiService {
    /**
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    suspend fun getBanners(): Response<List<Banner>>
}