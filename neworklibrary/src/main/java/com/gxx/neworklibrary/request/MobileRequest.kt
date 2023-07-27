package com.gxx.neworklibrary.request

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmSyncRequestType
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.base.AbsRequest

class MobileRequest(
    mOnOkHttpRequestManagerListener: OnOkHttpRequestManagerListener,
    mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener
) : AbsRequest(mOnOkHttpRequestManagerListener, mOnResponseBodyTransformJsonListener),
    OnMobileRequestListener {

    override suspend fun get(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                it.method,
                it.urlMap,
                null,
                EmRequestType.GET,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }

    }

    override fun getSync(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doSyncRequest(
                it.method,
                it.urlMap,
                null,
                EmSyncRequestType.GET_SYNC,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override suspend fun postBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmRequestType.POST,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override fun postSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doSyncRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmSyncRequestType.POST_SYNC,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override suspend fun postForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmRequestType.POST_FORM,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override fun postSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doSyncRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmSyncRequestType.POST_SYNC_FORM,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override suspend fun putBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmRequestType.PUT,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override fun putSyncBody(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doSyncRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmSyncRequestType.PUT_SYNC,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override suspend fun putForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmRequestType.PUT_FORM,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }

    override fun putSyncForm(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doSyncRequest(
                it.method,
                it.urlMap,
                it.bodyMap,
                EmSyncRequestType.PUT_SYNC_FORM,
                it.emResultType,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }


}