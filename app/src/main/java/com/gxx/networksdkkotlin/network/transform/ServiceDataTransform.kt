package com.gxx.networksdkkotlin.network.transform

import com.google.gson.JsonParser
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.error.impl.RootJsonEmptyApiException
import com.gxx.neworklibrary.error.impl.UnApiException
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.neworklibrary.util.Utils

/**
 * @author gaoxiaoxiong
 * @date 创建时间: 2023/8/12/012
 * 针对如下的json的，开发者可以自己改
 * {
 *  errorCode:0
 *  msg:"",
 *  data:[]或者{}或者无
 *  }
 **/
open class ServiceDataTransform : OnResponseBodyTransformJsonListener {
    companion object {
        const val ERROR_CODE = "errorCode"
        const val DATA = "data"
        const val ERROR_CODE_TYPE_0 = "0"//与服务器协商的正常状态
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description
     * @param jsString 服务器提供的根json
     * @param method 域名+端口+接口名称
     **/
    override fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean: BaseBean

        if (jsString.isEmpty()){
            throw RootJsonEmptyApiException()
        }

        val jsElement = JsonParser.parseString(jsString)

        if (jsElement.isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = jsElement.asJsonObject
            val errorCode = jsonObject.get(ERROR_CODE).asInt

            baseBean = if (errorCode.toString() == ERROR_CODE_TYPE_0) {
                if (jsonObject.get(DATA).isJsonArray) {
                    BaseBean(method, jsString, jsonObject.getAsJsonArray(DATA), errorCode.toString(),"")
                } else {
                    BaseBean(method, jsString, jsonObject.getAsJsonObject(DATA), errorCode.toString(),"")
                }
            } else {
                //与服务器协商的异常逻辑
                // 可以在这里抛异常，比如服务器有提供code = 201，那么可以抛出属于201的异常错误
               val baseUrl = Utils.getBaseUrlByMethod(method)
               val errorHandlerFactory = OkHttpManager.getRetrofitAndConfigModel(baseUrl)?.httpConfigModel?.errorHandlerFactory
                if (errorHandlerFactory!=null){
                    for (serviceErrorApiException in errorHandlerFactory.getServiceErrorApiExceptions()) {
                        if (serviceErrorApiException.code == errorCode.toString()){
                            serviceErrorApiException.jsString = jsString
                            serviceErrorApiException.errorMessage = "这里可以写错误的信息"
                            throw serviceErrorApiException
                        }
                    }
                }
               //未设置任何异常错误信息
                throw UnApiException(errorCode.toString(), jsString)
            }
        } else {
            //理论上这是永远不会触发的
            baseBean = BaseBean(method, jsString, jsElement, "-1","")
        }
        return baseBean
    }
}