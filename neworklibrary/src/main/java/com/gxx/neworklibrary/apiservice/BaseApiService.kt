package com.gxx.neworklibrary.apiservice

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface BaseApiService {
    /**
     * http://www.5mins-sun.com:8081/news?name=admin&pwd=123456
     * @param map map里面的Object 只能是String ，int
     */
    @GET
    fun getJson(
        @Url url: String,
        @QueryMap map: Map<String, Any>
    ): ResponseBody

    /**
     * RequestBody:
     * {
     * "albumID": 2,
     * "sectionID": 16
     * }
     */
    @POST
    fun postJsonBody(
        @Url url: String,
        @QueryMap map: Map<String, Any>,
        @Body body: RequestBody
    ): ResponseBody

    /**
     * Post请求, 以表单方式提交
     * user=admin
     * pwd=123456
     * @param map map里面的Object 只能是String ，int
     */
    @FormUrlEncoded
    @POST
    fun postForm(
        @Url ur: String,
        @QueryMap queryMap: Map<String, Any>,
        @FieldMap map: Map<String, Any>
    ): ResponseBody


    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description 上次，文件、Body、Url后面拼接参数
     */
    @Multipart
    @POST
    fun postFilesBodysUrl(
        @Url url: String,
        @Part files: List<Part>,
        @PartMap mapRequestBody: Map<String, RequestBody>,
        @QueryMap map: Map<String, Any>
    ): ResponseBody

    /**
     * @param file       存放在body里面
     * @param mapRequestBody 存放在body里面
     * @date: 2019/7/20 0020
     * @author: gaoxiaoxiong
     * @description:上传单个图片&一些参数
     */
    @Multipart
    @POST
    fun postSingleFileBodysUrl(
        @Url url: String,
        @Part file: Part,
        @PartMap mapRequestBody: Map<String, RequestBody>,
        @QueryMap map: Map<String, Any>
    ): ResponseBody


    /**
     * 作者：GaoXiaoXiong
     * 创建时间:2019/7/21
     * 注释描述:文件上传
     */
    @Multipart
    @POST
    fun uploadFile(
        @Url url: String,
        @Part file: Part,
        @PartMap mapRequestBody: Map<String, RequestBody>
    ): ResponseBody

    /**
     * @date: 2019/7/20 0020
     * @author: gaoxiaoxiong
     * RequestBody:
     * {
     * "albumID": 2,
     * "sectionID": 16
     * }
     */
    @PUT
    fun putJsonBody(
        @Url url: String,
        @QueryMap map: Map<String, Any>,
        @Body body: RequestBody
    ): ResponseBody
}