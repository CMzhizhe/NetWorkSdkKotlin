package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.model.RqParamModel

/**
 * @date 创建时间: 2023/7/23
 * @auther gaoxiaoxiong
 * @description 网络请求的封装
 **/
interface OnMobileRequestListener {
    suspend fun get(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    fun getSync(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    suspend fun postBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    fun postSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    suspend fun postForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    fun postSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    suspend fun putBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    fun putSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    suspend fun putForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )

    fun putSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener? = null,
        onRequestFailListener: OnRequestFailListener? = null
    )
}