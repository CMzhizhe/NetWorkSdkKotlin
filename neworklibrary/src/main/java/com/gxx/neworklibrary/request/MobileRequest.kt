package com.gxx.neworklibrary.request

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.inter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody

/**
 * @date 创建时间: 2023/7/21
 * @auther gxx
 * @description 请求的封装
 **/
class MobileRequest : OnMobileRequestListener {
    private val TAG = "MobileRequest"
    private val mGson = Gson()

    private var mOnOkHttpRequestManagerListener: OnOkHttpRequestManagerListener? = null
    private var mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener? = null

    protected val REQUEST_RESULT_OWN = 1 //自己处理类型

    protected val REQUEST_RESULT_OBJECT = 2 //只返回对象

    protected val REQUEST_RESULT_ARRAY = 3 //只返回数组

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 拿到 OkHttpRequestManager 实体
     **/
    fun setOnOkHttpRequestManagerListener(listener: OnOkHttpRequestManagerListener) {
        this.mOnOkHttpRequestManagerListener = listener
    }

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 将 ResponseBody 转成 json
     **/
    fun setOnResponseBodyTransformJsonListener(listener: OnResponseBodyTransformJsonListener) {
        this.mOnResponseBodyTransformJsonListener = listener
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description  get 请求
     * @param method 整个接口名称，包含域名在内
     * @param urlMapString 拼接在url后面
     * @param  isSync true 同步请求
     * @param  requestResultValue 希望服务器给的集合的结果
     */
    override suspend fun doGetRequest(
        method: String,
        urlMap: Map<String, Any>,
        isSync: Boolean,
        requestResultValue: Int,
        onRequestSuccessListener: OnRequestSuccessListener,
        onRequestFailListener: OnRequestFailListener
    ) {
        if (method.isEmpty()) {
            throw IllegalStateException("method 是空的")
        }

        if (mOnOkHttpRequestManagerListener == null) {
            throw IllegalStateException("OnOkHttpRequestManagerListener 没有实现")
        }

        if (mOnResponseBodyTransformJsonListener == null) {
            throw IllegalStateException("OnResponseBodyTransformJsonListener 没有实现")
        }

         doComposeMapRequest(method, urlMap,onRequestFailListener).collect {
             doIParseResult(method,requestResultValue, listener = it,onRequestSuccessListener,onRequestFailListener)
         }
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 异步请求
     **/
    private suspend fun doComposeMapRequest(
        method: String,
        urlMap: Map<String, Any>,
        onRequestFailListener: OnRequestFailListener
    ): Flow<OnIParserListener> {
        val responseBody = mOnOkHttpRequestManagerListener!!
            .onGetOkHttpRequestManager()
            .createBaseApi()
            .getJson(method, urlMap)

        return flow<String> {
            emit(responseBody.string())
        }.transform<String, OnIParserListener> {
            emit(mOnResponseBodyTransformJsonListener!!.onResponseBodyTransformJson(method, it))
        }.catch {
            it.printStackTrace()
            onRequestFailListener.onRequestFail(
                it,
                null,
                null,
                null,
                null
            )
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 处理结果
     **/
    private fun doIParseResult(
        method: String,
        requestResultValue: Int,
        listener: OnIParserListener,
        onRequestSuccessListener: OnRequestSuccessListener,
        onRequestFailListener: OnRequestFailListener
    ) {
        if (mOnOkHttpRequestManagerListener == null) {
            return
        }
        if (requestResultValue == REQUEST_RESULT_OWN) {//自己处理
            onRequestSuccessListener
                .onRequestSuccess(method, listener.resultDataJsonElement(), listener)
        } else {
            if (listener.isSuccess()) {
                if (listener.resultDataJsonElement() == null) {
                    onRequestSuccessListener.onRequestSuccess(method, null, listener)
                } else {
                    if (requestResultValue == REQUEST_RESULT_OBJECT) {//希望拿到对象
                        if (listener.resultDataJsonElement() is JsonObject) {
                            onRequestSuccessListener.onRequestSuccess(
                                method,
                                listener.resultDataJsonElement(),
                                listener
                            )
                        } else {
                            onRequestFailListener.onRequestFail(
                                null,
                                null,
                                null,
                                null,
                                listener
                            )
                        }
                    } else if (requestResultValue == REQUEST_RESULT_ARRAY) {//希望拿到数组
                        if (listener.resultDataJsonElement() is JsonArray) {
                            onRequestSuccessListener.onRequestSuccess(
                                method,
                                listener.resultDataJsonElement(),
                                listener
                            )
                        } else {
                            onRequestFailListener.onRequestFail(
                                null,
                                null,
                                null,
                                null,
                                listener
                            )
                        }
                    }
                }
            } else {//失败处理
                onRequestFailListener.onRequestFail(
                    null,
                    null,
                    null,
                    null,
                    listener
                )
            }
        }
    }


}