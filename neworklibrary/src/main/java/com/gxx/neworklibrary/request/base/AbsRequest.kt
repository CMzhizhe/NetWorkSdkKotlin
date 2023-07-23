package com.gxx.neworklibrary.request.base

import com.google.gson.Gson
import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.constans.EmSyncRequestType
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import com.gxx.neworklibrary.util.MultipartBodyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


/**
 * @date 创建时间: 2023/7/21
 * @auther gxx
 * @description 请求的封装
 **/
abstract class AbsRequest(
    private val mOnOkHttpRequestManagerListener: OnOkHttpRequestManagerListener,
    private val mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener
) : OnRequestListener {
    private val TAG = "MobileRequest"
    private val mGson = Gson()
    private val mJsonParseResult = JsonParseResult()
    private val mMultipartBodyUtils = MultipartBodyUtils()


    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description  get 请求
     * @param method 整个接口名称，包含域名在内
     * @param urlMapString 拼接在url后面
     * @param bodyMap 放到body里面
     * @param emRequestType 请求类型
     * @param emResultType 希望的结果
     * @param  requestResultValue 希望服务器给的集合的结果
     */
    override suspend fun doRequest(
        method: String,
        urlMap: Map<String, Any>?,
        bodyMap: Map<String, Any>?,
        emRequestType: EmRequestType,
        emResultType: EmResultType,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {


        doComposeMapRequest(
            method,
            urlMap ?: mutableMapOf(),
            bodyMap ?: mutableMapOf(),
            emRequestType,
            onRequestFailListener
        ).collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                method,
                emResultType,
                listener = it,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override fun doSyncRequest(
        method: String,
        urlMap: Map<String, Any>?,
        bodyMap: Map<String, Any>?,
        emRequestType: EmSyncRequestType,
        emResultType: EmResultType,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        if (method.isEmpty()) {
            throw IllegalStateException("method 是空的")
        }
       val listener = doSyncComposeMapRequest(method,urlMap?: mutableMapOf(),bodyMap?: mutableMapOf(),emRequestType,onRequestFailListener)
           ?: return
        mJsonParseResult.doIParseResult(
            method,
            emResultType,
            listener,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 异步请求
     * @param method 请求的方法名称
     * @param urlMap 连接后面的参数
     * @param bodyMap 针对post,put使用
     * @param emRequestType 请求的类型
     **/
    private suspend fun doComposeMapRequest(
        method: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        bodyMap: Map<String, Any> = mutableMapOf(),
        emRequestType: EmRequestType,
        onRequestFailListener: OnRequestFailListener?
    ): Flow<OnIParserListener?> {
        val aipService = mOnOkHttpRequestManagerListener
            .onGetOkHttpRequestManager()
            .createBaseApi()
        val responseBody = when (emRequestType) {
            EmRequestType.GET -> {
                aipService.getJson(method, urlMap ?: mutableMapOf())
            }

            EmRequestType.POST -> {
                if (bodyMap.isNotEmpty()) {
                    aipService.postJson(
                            method,
                            urlMap,
                            mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap))
                        )
                } else {
                    aipService.postJson(method, urlMap ?: mutableMapOf())
                }
            }

            EmRequestType.POST_FORM -> {
                if (bodyMap.isNotEmpty()) {
                    aipService.postForm(method, urlMap, bodyMap)
                } else {
                    aipService.postForm(method, urlMap)
                }
            }

            EmRequestType.PUT -> {
                if (bodyMap.isNotEmpty()) {
                    aipService.putJson(
                            method,
                            urlMap,
                            mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap))
                        )
                } else {
                    aipService.putJson(method, urlMap)
                }
            }

            EmRequestType.PUT_FORM -> {
                if (bodyMap.isNotEmpty()) {
                    aipService
                        .putForm(method, urlMap, bodyMap)
                } else {
                    aipService
                        .putForm(method, urlMap)
                }
            }
        }


        return flow<String> {
            emit(responseBody.string())
        }.transform<String, OnIParserListener?> {
            emit(mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, it))
        }.catch {
            it.printStackTrace()
            onRequestFailListener?.onRequestFail(
                it,
                null,
                null,
                null,
                null
            )
            emit(null)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 同步请求
     **/
    private fun doSyncComposeMapRequest(
        method: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        bodyMap: Map<String, Any> = mutableMapOf(),
        emRequestType: EmSyncRequestType,
        onRequestFailListener: OnRequestFailListener?
    ): OnIParserListener? {
        var onIParserListener: OnIParserListener? = null
        kotlin.runCatching {
            val aipService = mOnOkHttpRequestManagerListener
                .onGetOkHttpRequestManager()
                .createBaseApi()
            val responseBody = when (emRequestType) {
                EmSyncRequestType.GET_SYNC -> {
                    aipService
                        .getSyncJson(method, urlMap)
                }
                EmSyncRequestType.POST_SYNC -> {
                    if (bodyMap.isNotEmpty()){
                        aipService
                            .postSyncJson(method,urlMap,mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap)))
                    }else{
                        aipService
                            .postSyncJson(method,urlMap)
                    }
                }

                EmSyncRequestType.POST_SYNC_FORM -> {
                    if (bodyMap.isNotEmpty()){
                        aipService.postSyncForm(method,urlMap,bodyMap)
                    }else{
                        aipService.postSyncForm(method,urlMap)
                    }
                }
                EmSyncRequestType.PUT_SYNC -> {
                    if (bodyMap.isNotEmpty()){
                        aipService.putSyncJson(method,urlMap,mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap)))
                    }else{
                        aipService.putSyncJson(method,urlMap)
                    }
                }
                EmSyncRequestType.PUT_SYNC_FORM -> {
                    if (bodyMap.isNotEmpty()){
                        aipService.postSyncForm(method,urlMap,bodyMap)
                    }else{
                        aipService.postSyncForm(method,urlMap)
                    }
                }
            }
            val jsString = responseBody.string()
            onIParserListener = mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, jsString)
        }.onFailure {
            it.printStackTrace()
            onRequestFailListener?.onRequestFail(
                it,
                null,
                null,
                null,
                null
            )
        }
        return onIParserListener
    }

}