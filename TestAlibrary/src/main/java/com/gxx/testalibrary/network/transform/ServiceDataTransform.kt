package com.gxx.testalibrary.network.transform

import com.google.gson.JsonParser
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import com.gxx.testalibrary.bean.BaseBean

class ServiceDataTransform : OnResponseBodyTransformJsonListener {
    companion object {
        const val ERROR_CODE = "errorCode"
        const val DATA = "data"
    }

    override fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean: BaseBean
        if (JsonParser.parseString(jsString).isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = JsonParser.parseString(jsString).asJsonObject
            val errorCode = jsonObject.get(ERROR_CODE).asInt
            baseBean = if (jsonObject.get(DATA).isJsonArray) {
                BaseBean(method, jsString, jsonObject.getAsJsonArray(DATA), errorCode)
            } else {
                BaseBean(method, jsString, jsonObject.getAsJsonObject(DATA), errorCode)
            }
        } else {
            baseBean = BaseBean(method, jsString, null, -1)
        }
        return baseBean
    }
}