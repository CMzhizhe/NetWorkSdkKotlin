package com.gxx.neworklibrary.request

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmSyncRequestType
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.base.AbsRequest

/**
 * @date 创建时间: 2023/7/31
 * @auther gaoxiaoxiong
 * @description 网络请求封装
 * @param mOnResponseBodyTransformJsonListener 服务器给的参数，回传给调用者
 * @param mOnBaseApiServiceListener 获取BaseApiService
 **/
class MobileRequest(
    mOnBaseApiServiceListener:OnBaseApiServiceListener,
    mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener
) : AbsRequest(mOnBaseApiServiceListener,mOnResponseBodyTransformJsonListener),
    OnMobileRequestListener {

    override suspend fun get(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doRequest(
            rqParamModel,
            EmRequestType.GET,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override fun getSync(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doSyncRequest(
            rqParamModel,
            EmSyncRequestType.GET_SYNC,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override suspend fun postBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doRequest(
            rqParamModel,
            EmRequestType.POST,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override fun postSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doSyncRequest(
            rqParamModel,
            EmSyncRequestType.POST_SYNC,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override suspend fun postForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doRequest(
            rqParamModel,
            EmRequestType.POST_FORM,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override fun postSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doSyncRequest(
            rqParamModel,
            EmSyncRequestType.POST_SYNC_FORM,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override suspend fun putBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doRequest(
            rqParamModel,
            EmRequestType.PUT,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override fun putSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doSyncRequest(
            rqParamModel,
            EmSyncRequestType.PUT_SYNC,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override suspend fun putForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doRequest(
            rqParamModel,
            EmRequestType.PUT_FORM,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }

    override fun putSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        doSyncRequest(
            rqParamModel,
            EmSyncRequestType.PUT_SYNC_FORM,
            onRequestSuccessListener,
            onRequestFailListener
        )
    }


}