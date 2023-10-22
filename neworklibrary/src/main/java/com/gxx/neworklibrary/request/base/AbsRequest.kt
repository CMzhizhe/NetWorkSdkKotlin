package com.gxx.neworklibrary.request.base

import com.google.gson.Gson
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.error.impl.NoNetWorkApiException
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import com.gxx.neworklibrary.util.MultipartBodyUtils
import com.gxx.neworklibrary.util.NetWorkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
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
        if (rqParamModel.hostUrl.isEmpty()) {
            throw IllegalStateException("baseUrl 是空的")
        }

        if (rqParamModel.funName.isEmpty()) {
            throw IllegalStateException("funName 是空的")
        }

        if (!NetWorkUtil.isNetConnected()) {
            if (currentCoroutineContext().isActive){
                withContext(Dispatchers.Main){
                    onRequestFailListener?.onRequestFail("${rqParamModel.hostUrl}${rqParamModel.funName}",NoNetWorkApiException())
                }
            }
            return
        }

        doComposeMapRequest(
            rqParamModel.hostUrl,
            rqParamModel.funName,
            rqParamModel.urlMap ?: mutableMapOf(),
            rqParamModel.bodyMap ?: mutableMapOf(),
            emRequestType,
            onRequestFailListener
        )?.collect {
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${rqParamModel.hostUrl}${rqParamModel.funName}",
                rqParamModel.emResultType,
                listener = it,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
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
    ): Flow<OnIParserListener?>? {
        val method = "${baseUrl}${funName}"
        val aipService = OkHttpManager.getInstance().getRetrofit(baseUrl)?.create(BaseApiService::class.java)?:return null
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
                        aipService.putForm(method, urlMap, bodyMap)
                    } else {
                        aipService.putForm(method, urlMap)
                    }
                }
            }
        }.onFailure {
            it.printStackTrace()
            if (currentCoroutineContext().isActive){
                withContext(Dispatchers.Main){
                    onRequestFailListener?.onRequestFail(
                        method,
                        it,
                        null,
                        null,
                        null,
                        null
                    )
                }
            }
        }

       if (!currentCoroutineContext().isActive){
           return null
       }

        return responseBodyTransformJson(baseUrl, funName, responseBody, onRequestFailListener)
    }

    /**
     * @date 创建时间: 2023/8/7
     * @auther gaoxiaoxiong
     * 将 responseBody 转 Flow<OnIParserListener?>
     * @param baseUrl 请求的baseUrl
     * @param funName 接口名称
     * @param responseBody 服务器提供的数据
     * @param onRequestFailListener 失败的回调
     **/
    suspend fun responseBodyTransformJson(
        baseUrl: String,
        funName: String,
        responseBody: ResponseBody? = null,
        onRequestFailListener: OnRequestFailListener? = null
    ): Flow<OnIParserListener?> {
        val method = "${baseUrl}${funName}"
        if (responseBody == null) {
            return flow {
                emit(null)
            }
        } else {
            return flow<String> {
                emit(responseBody.string())
            }.transform<String, OnIParserListener?> {
                emit(mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, it))
            }.catch {
                it.printStackTrace()
                if (currentCoroutineContext().isActive){
                    withContext(Dispatchers.Main){
                        onRequestFailListener?.onRequestFail(
                            method,
                            it,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                }
            }.flowOn(Dispatchers.Default)
        }
    }

}