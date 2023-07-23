package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.constans.EmSyncRequestType


interface OnRequestListener {

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description
     * @param method 整个接口名称，包含域名在内
     * @param urlMapString 拼接在url后面
     * @param bodyMap 放到body里面
     * @param emRequestType 请求类型
     * @param emResultType 希望的结果
     * @param  requestResultValue 希望服务器给的集合的结果
     */
   suspend fun doRequest(
        method: String,
        urlMap: Map<String, Any>?=null,
        bodyMap: Map<String, Any>?=null,
        emRequestType: EmRequestType,
        emResultType: EmResultType,
        onRequestSuccessListener: OnRequestSuccessListener,
        onRequestFailListener: OnRequestFailListener
    )

    fun doSyncRequest(
        method: String,
        urlMap: Map<String, Any>?=null,
        bodyMap: Map<String, Any>?=null,
        emRequestType: EmSyncRequestType,
        emResultType: EmResultType,
        onRequestSuccessListener: OnRequestSuccessListener,
        onRequestFailListener: OnRequestFailListener
    )
}