package com.gxx.networksdkkotlin.network.pase

import com.google.gson.JsonElement
import com.gxx.networksdkkotlin.util.MoshiUtil
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.resultcall.AbsRequestResultImpl
import com.squareup.moshi.JsonAdapter
import java.lang.reflect.ParameterizedType

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 服务器数据处理
 **/
open class DataParseSuFaCall<T> : AbsRequestResultImpl() {
    private val TAG = "ServiceDataParse"

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
                onRequestFail(e,"","解析异常", null,onIParserListener)
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
        throwable: Throwable?,
        status: String?,
        failMsg: String?,
        errorJsonString: String?,
        onIParserListener: OnIParserListener?
    ) {
        if (throwable!=null){
            val resPoneThrowable = ErrorHandlerFactory.netWorkException(throwable)
            //自定义解析错误处理
            if (resPoneThrowable.code == ExceptionHandle.ERROR.UNKNOWN.toString() && throwable is AbsApiException){
                ErrorHandlerFactory.propaGateError(ErrorHandlerFactory.getErrorHandlers().first(),throwable)
            }
        }
        onRequestDataFail(status?:"", failMsg?:"", onIParserListener as BaseBean?)
        onRequestBaseBeanFail(onIParserListener as BaseBean? )
    }

    /**
     * @date 2022/5/7
     * @auther qinzhichang
     * @Descriptiion 请求失败
     **/
    open fun onRequestDataFail(code: String, msg: String, baseBean: BaseBean?=null) {}

    /**
     * @date 2022/6/23
     * @author qinzhichang
     * @Description 请求失败
     **/
    open fun onRequestBaseBeanFail(baseBean: BaseBean?=null) {}


    /**
     * @date 2022/5/7
     * @auther qinzhichang
     * @Descriptiion 请求成功
     * 返回最终的结果T
     **/
    open fun onRequestDataSuccess(data: T?) {}

    /**
     * @date 2022/5/7
     * @auther qinzhichang
     * @Descriptiion 请求成功
     * 返回含有 BaseBean 的
     **/
    open fun onRequestBaseBeanSuccess(data: T?, baseBean: BaseBean) {}
}