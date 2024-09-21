package com.gxx.networksdkkotlin.network.parse

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.JsonElement
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnRequestFailListener
import com.gxx.neworklibrary.inter.OnRequestSuccessListener
import com.gxx.neworklibrary.util.Utils
import java.lang.reflect.ParameterizedType

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 服务器数据处理
 **/
open class ServiceDataParseCall<T> : OnRequestSuccessListener, OnRequestFailListener {
    private val TAG = "ServiceDataParseCall"

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 成功结果回调
     **/
    override suspend fun onRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    ) {
        if (targetElement!=null){
            try {
                val parameterizedType = this::class.java.genericSuperclass as ParameterizedType
                val subType =  parameterizedType.actualTypeArguments.first() //获取泛型T
                val result:Any? = GsonUtils.fromJson(targetElement.toString(),subType)
                onRequestBaseBeanSuccess(if (result == null) null else result as T, onIParserListener as BaseBean)
            } catch (e: Exception) {
                e.printStackTrace()
                onRequestFail(method,e,onIParserListener)
            }
        }else{
            onRequestBaseBeanSuccess(null, onIParserListener as BaseBean)
        }
    }



    override suspend fun onRequestFail(
        method: String,
        exception: Throwable?,
        onIParserListener: OnIParserListener?
    ) {
        if (exception!=null){
            val baseUrl = Utils.getBaseUrlByMethod(method)
            val cacheHandler = OkHttpManager.getRetrofitAndConfigModel(baseUrl)?.httpConfigModel?.errorHandlerFactory
            if (cacheHandler!=null){
                if (exception is AbsApiException){//处理服务器的异常
                    cacheHandler.rollServiceCodeGateError(cacheHandler.getServiceErrorHandlers().first(),exception)
                }else{//处理网络异常
                    cacheHandler.netWorkException(exception)
                }
            }
        }
        onRequestDataFail(onIParserListener as BaseBean?)
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求失败
     **/
    open suspend fun onRequestDataFail(baseBean: BaseBean?=null) {}



   /**
    * @author gaoxiaoxiong
    * @date 创建时间: 2023/8/6/006
    * @description  请求成功
    * 返回含有 BaseBean 的
    **/
    open suspend fun onRequestBaseBeanSuccess(data: T?, baseBean: BaseBean) {}

}