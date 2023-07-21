package com.gxx.neworklibrary.apiservice

import kotlinx.coroutines.flow.Flow
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
   suspend fun getJson(
        @Url url: String,
        @QueryMap map: Map<String, Any>
    ): Flow<ResponseBody>

}