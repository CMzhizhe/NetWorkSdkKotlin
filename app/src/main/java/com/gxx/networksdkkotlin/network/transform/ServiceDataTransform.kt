package com.gxx.networksdkkotlin.network.transform

import com.google.gson.JsonParser
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.networksdkkotlin.network.error.exception.LoginApiException
import com.gxx.networksdkkotlin.network.error.exception.PayApiException
import com.gxx.networksdkkotlin.network.error.exception.TokenApiException
import com.gxx.networksdkkotlin.network.error.exception.UnApiException
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import okhttp3.ResponseBody

class ServiceDataTransform : OnResponseBodyTransformJsonListener {
    companion object {
        const val ERROR_CODE = "errorCode"
        const val DATA = "data"
        const val ERROR_CODE_TYPE_0 = "0"//与服务器协商的正常状态
        const val ERROR_CODE_TYPE_101 = "101"//与服务器协商错误的逻辑
        const val ERROR_CODE_TYPE_102 = "102"//与服务器协商错误的逻辑
        const val ERROR_CODE_TYPE_103 = "103"//与服务器协商错误的逻辑
    }

    override fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean: BaseBean
        if (JsonParser.parseString(jsString).isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = JsonParser.parseString(jsString).asJsonObject
            val errorCode = jsonObject.get(ERROR_CODE).asInt
            baseBean = if (errorCode.toString() == ERROR_CODE_TYPE_0) {
                if (jsonObject.get(DATA).isJsonArray) {
                    BaseBean(method, jsString, jsonObject.getAsJsonArray(DATA), errorCode)
                } else {
                    BaseBean(method, jsString, jsonObject.getAsJsonObject(DATA), errorCode)
                }
            } else {//与服务器协商的异常逻辑
                // 可以在这里抛异常
                if (errorCode.toString() == ERROR_CODE_TYPE_101) {
                    throw LoginApiException(errorCode.toString(), jsString, "登陆的异常")
                } else if (errorCode.toString() == ERROR_CODE_TYPE_102) {
                    throw PayApiException(errorCode.toString(), jsString, "支付的异常")
                } else if (errorCode.toString() == ERROR_CODE_TYPE_103) {
                    throw TokenApiException(errorCode.toString(), jsString, "token的异常")
                } else {
                    throw UnApiException(errorCode.toString(), jsString, "未跟服务器定义的异常")
                }
            }
        } else {
            baseBean = BaseBean(method, jsString, null, -1)
        }
        return baseBean
    }
}