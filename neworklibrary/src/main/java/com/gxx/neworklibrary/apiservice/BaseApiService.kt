package com.gxx.neworklibrary.apiservice

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 基类Base配置
 **/
interface BaseApiService {
    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description
     */
    @JvmSuppressWildcards
    @GET
    fun javaGetJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): Call<ResponseBody>

    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description
     */
    @JvmSuppressWildcards
    @POST
    fun javaPostJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): Call<ResponseBody>

    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description
     */
    @JvmSuppressWildcards
    @POST
    fun javaPostJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>,
        @Body body: Map<String, Any>
    ): Call<ResponseBody>


    @JvmSuppressWildcards
    @GET
    suspend fun getJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody

    @JvmSuppressWildcards
    @POST
    suspend fun postJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody


    @JvmSuppressWildcards
    @POST
    suspend fun postJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>,
        @Body body: RequestBody
    ): ResponseBody

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST
    suspend fun postForm(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST
    suspend fun postForm(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>,
        @FieldMap fieldMap :Map<String, Any>
    ): ResponseBody

    //post--------------finish

    @JvmSuppressWildcards
    @PUT
    suspend fun putJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody


    @JvmSuppressWildcards
    @PUT
    suspend fun putJson(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>,
        @Body body: RequestBody
    ): ResponseBody


    @JvmSuppressWildcards
    @FormUrlEncoded
    @PUT
    suspend fun putForm(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>
    ): ResponseBody

    @JvmSuppressWildcards
    @FormUrlEncoded
    @PUT
    suspend fun putForm(
        @Url url: String,
        @QueryMap urlMap: Map<String, Any>,
        @FieldMap fieldMap :Map<String, Any>
    ): ResponseBody

}