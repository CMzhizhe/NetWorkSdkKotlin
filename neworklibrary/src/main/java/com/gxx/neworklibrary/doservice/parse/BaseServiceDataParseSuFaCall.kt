package com.gxx.neworklibrary.doservice.parse

import com.google.gson.JsonElement
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.model.BaseBean
import com.gxx.neworklibrary.resultcall.AbsRequestResultImpl
import com.gxx.neworklibrary.util.MoshiUtil
import com.gxx.neworklibrary.util.Utils
import com.squareup.moshi.JsonAdapter
import java.lang.reflect.ParameterizedType

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 服务器数据处理
 **/
open class BaseServiceDataParseSuFaCall<T> : AbsRequestResultImpl() {
    private val TAG = "DataParseSuFaCall"

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 成功结果回调
     **/
    override fun onRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    ) {
        if (targetElement!=null){
            var result:Any?=null
            try {
                val parameterizedType = this::class.java.genericSuperclass as ParameterizedType
                val subType =  parameterizedType.actualTypeArguments.first() //获取泛型T
                val adapter: JsonAdapter<Any> = MoshiUtil.moshi.adapter(subType)
                result = adapter.fromJson(targetElement.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                //处理解析异常
                onRequestFail(method,e,"","解析异常", null,onIParserListener)
                return
            }

            onRequestDataSuccess(if (result == null) null else result as T)
            onRequestBaseBeanSuccess(if (result == null) null else result as T,
                onIParserListener as BaseBean
            )
        }else{
            onRequestDataSuccess(null)
            onRequestBaseBeanSuccess(null, onIParserListener as BaseBean)
        }
    }

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 失败接口的调用
     **/
    override fun onRequestFail(
        method: String,
        throwable: Throwable?,
        status: String?,
        failMsg: String?,
        errorJsonString: String?,
        onIParserListener: OnIParserListener?
    ) {
        if (throwable!=null){
            val baseUrl = Utils.getBaseUrlByMethod(method)
            val cacheHandler = OkHttpManager.getErrorHandlerFactory(baseUrl)
            if (cacheHandler!=null){
                if (throwable is AbsApiException){//处理服务器的异常
                    cacheHandler.rollServiceCodeGateError(cacheHandler.getServiceErrorHandlers().first(),throwable)
                }else{//处理网络异常
                    cacheHandler.netWorkException(throwable)
                }
            }
        }
        onRequestDataFail(status?:"", failMsg?:"", onIParserListener as BaseBean?)
        onRequestBaseBeanFail(onIParserListener as BaseBean? )
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求失败
     **/
    open fun onRequestDataFail(code: String, msg: String, baseBean: BaseBean?=null) {}

      /**
       * @author gaoxiaoxiong
       * @date 创建时间: 2023/8/6/006
       * @description  请求失败
       **/
    open fun onRequestBaseBeanFail(baseBean: BaseBean?=null) {}


    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求成功
     **/
    open fun onRequestDataSuccess(data: T?) {}

   /**
    * @author gaoxiaoxiong
    * @date 创建时间: 2023/8/6/006
    * @description  请求成功
    * 返回含有 BaseBean 的
    **/
    open fun onRequestBaseBeanSuccess(data: T?, baseBean: BaseBean) {}
}