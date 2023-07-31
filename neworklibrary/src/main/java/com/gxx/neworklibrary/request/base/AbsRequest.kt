package com.gxx.neworklibrary.request.base

import com.google.gson.Gson
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmSyncRequestType
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import com.gxx.neworklibrary.util.MultipartBodyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody


/**
 * @date 创建时间: 2023/7/21
 * @auther gxx
 * @description 请求的封装
 * @param mOnResponseBodyTransformJsonListener 处理服务器提供的数据，转换成，自己需要的baseBean
 **/
abstract class AbsRequest(
    private val mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener
) : OnRequestListener {
    private val TAG = "MobileRequest"
    private val mGson = Gson()
    private val mJsonParseResult = JsonParseResult()
    private val mMultipartBodyUtils = MultipartBodyUtils()


    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description  异步 请求
     * @param rqParamModel 请求参数的封装
     * @param emRequestType 请求类型
     */
    override suspend fun doRequest(
        rqParamModel: RqParamModel,
        emRequestType: EmRequestType,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        if (rqParamModel.baseUrl.isEmpty()) {
            throw IllegalStateException("baseUrl 是空的")
        }

        if (rqParamModel.funName.isEmpty()) {
            throw IllegalStateException("funName 是空的")
        }

        doComposeMapRequest(
            rqParamModel.baseUrl,
            rqParamModel.funName,
            rqParamModel.urlMap ?: mutableMapOf(),
            rqParamModel.bodyMap ?: mutableMapOf(),
            emRequestType,
            onRequestFailListener
        ).collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${rqParamModel.baseUrl}${rqParamModel.funName}",
                rqParamModel.emResultType,
                listener = it,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }


    override fun doSyncRequest(
        rqParamModel: RqParamModel,
        emRequestType: EmSyncRequestType,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        if (rqParamModel.baseUrl.isEmpty()) {
            throw IllegalStateException("baseUrl 是空的")
        }

        if (rqParamModel.funName.isEmpty()) {
            throw IllegalStateException("funName 是空的")
        }

        val listener = doSyncComposeMapRequest(
            rqParamModel.baseUrl,
            rqParamModel.funName,
            rqParamModel.urlMap ?: mutableMapOf(),
            rqParamModel.bodyMap ?: mutableMapOf(),
            emRequestType,
            onRequestFailListener
        ) ?: return
        mJsonParseResult.doIParseResult(
            "${rqParamModel.baseUrl}${rqParamModel.funName}",
            rqParamModel.emResultType,
            listener,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }


    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 异步请求
     * @param baseUrl baseUrl
     * @param funName 接口名称
     * @param urlMap 连接后面的参数
     * @param bodyMap 针对post,put使用
     * @param emRequestType 请求的类型
     **/
    private suspend fun doComposeMapRequest(
        baseUrl: String,
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        bodyMap: Map<String, Any> = mutableMapOf(),
        emRequestType: EmRequestType,
        onRequestFailListener: OnRequestFailListener?
    ): Flow<OnIParserListener?> {
        val method = "${baseUrl}${funName}"
        val aipService = OkHttpManager.getBaseApiService(baseUrl)
        var responseBody: ResponseBody? = null
        kotlin.runCatching {
            responseBody = when (emRequestType) {
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
        }.onFailure {
            it.printStackTrace()
            withContext(Dispatchers.Main){
                onRequestFailListener?.onRequestFail(
                    it,
                    null,
                    null,
                    null,
                    null
                )
            }
        }
        if (responseBody == null) {
            return flow {
                emit(null)
            }
        } else {
            return flow<String> {
                emit(responseBody!!.string())
            }.transform<String, OnIParserListener?> {
                emit(mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, it))
            }.catch {
                it.printStackTrace()
                withContext(Dispatchers.Main){
                    onRequestFailListener?.onRequestFail(
                        it,
                        null,
                        null,
                        null,
                        null
                    )
                }
                emit(null)
            }.flowOn(Dispatchers.IO)
        }
    }

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 同步请求
     **/
    private fun doSyncComposeMapRequest(
        baseUrl: String,
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        bodyMap: Map<String, Any> = mutableMapOf(),
        emRequestType: EmSyncRequestType,
        onRequestFailListener: OnRequestFailListener?
    ): OnIParserListener? {
        val method = "${baseUrl}${funName}"
        var onIParserListener: OnIParserListener? = null
        kotlin.runCatching {
            val aipService = OkHttpManager
                .getBaseApiService(baseUrl)
            val responseBody = when (emRequestType) {
                EmSyncRequestType.GET_SYNC -> {
                    aipService
                        .getSyncJson(method, urlMap)
                }

                EmSyncRequestType.POST_SYNC -> {
                    if (bodyMap.isNotEmpty()) {
                        aipService
                            .postSyncJson(
                                method,
                                urlMap,
                                mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap))
                            )
                    } else {
                        aipService
                            .postSyncJson(method, urlMap)
                    }
                }

                EmSyncRequestType.POST_SYNC_FORM -> {
                    if (bodyMap.isNotEmpty()) {
                        aipService.postSyncForm(method, urlMap, bodyMap)
                    } else {
                        aipService.postSyncForm(method, urlMap)
                    }
                }

                EmSyncRequestType.PUT_SYNC -> {
                    if (bodyMap.isNotEmpty()) {
                        aipService.putSyncJson(
                            method,
                            urlMap,
                            mMultipartBodyUtils.jsonToRequestBody(mGson.toJson(bodyMap))
                        )
                    } else {
                        aipService.putSyncJson(method, urlMap)
                    }
                }

                EmSyncRequestType.PUT_SYNC_FORM -> {
                    if (bodyMap.isNotEmpty()) {
                        aipService.postSyncForm(method, urlMap, bodyMap)
                    } else {
                        aipService.postSyncForm(method, urlMap)
                    }
                }
            }
            val jsString = responseBody.string()
            onIParserListener =
                mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, jsString)
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