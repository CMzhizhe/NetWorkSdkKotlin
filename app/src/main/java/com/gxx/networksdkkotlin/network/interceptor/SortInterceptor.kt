package com.gxx.networksdkkotlin.network.interceptor

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.gxx.neworklibrary.constans.Constant
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.util.Collections


/**
  * 排序
  */
class SortInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        if (originalRequest.method == "POST"){
            val requestBody = originalRequest.body
            if (requestBody!=null){
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                val jsonObject = JSONObject(buffer.readByteString().utf8())
                val linkHashMap:java.util.LinkedHashMap<String,Any> = GsonUtils.fromJson(jsonObject.toString(),object : TypeToken<LinkedHashMap<String, Any>>() {}.type)
                //这里将map.entrySet()转换成list
                val sortedKeys: List<String> = ArrayList<String>(linkHashMap.keys)
                Collections.sort(sortedKeys)
                val sortedMap:java.util.LinkedHashMap<String,Any>  = LinkedHashMap<String, Any>()
                for (key in sortedKeys){
                    sortedMap[key] = linkHashMap[key] as Any
                }
                builder.post(GsonUtils.toJson(sortedMap).toRequestBody(Constant.APPLICATION_JSON_UTF8.toMediaType()))
            }
        }
        val request = builder.build()
        return chain.proceed(request)
    }
}