package com.gxx.networksdkkotlin.network.transform

import com.google.gson.JsonParser
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import okhttp3.ResponseBody

class ServiceDataTransform: OnResponseBodyTransformJsonListener {
    override suspend fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean:BaseBean
        if (JsonParser.parseString(jsString).isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = JsonParser.parseString(jsString).asJsonObject
            val errorCode = jsonObject.get("errorCode").asInt
            if (jsonObject.get("data").isJsonArray){
                baseBean =  BaseBean(method,jsString,jsonObject.getAsJsonArray("data"),errorCode)
            }else{
                baseBean =  BaseBean(method,jsString,jsonObject.getAsJsonObject("data"),errorCode)
            }
        }  else{
            baseBean =  BaseBean(method,jsString,null,-1)
        }
        return baseBean
    }
}