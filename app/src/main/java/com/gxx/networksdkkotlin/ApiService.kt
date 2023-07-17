package com.gxx.networksdkkotlin

import com.gxx.neworklibrary.Response
import com.gxx.neworklibrary.bean.Banner
import com.gxx.neworklibrary.bean.Friend
import retrofit2.http.GET

interface ApiService {
    /**
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    suspend fun getBanners(): Response<List<Banner>>
    //顺手加一个
    @GET("friend/json")
    suspend fun getFriends(): Response<List<Friend>>
}
