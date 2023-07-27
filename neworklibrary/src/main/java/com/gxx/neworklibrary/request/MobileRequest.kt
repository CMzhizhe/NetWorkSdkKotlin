package com.gxx.neworklibrary.request

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.constans.EmSyncRequestType
import com.gxx.neworklibrary.inter.*
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.base.AbsRequest

class MobileRequest(
    mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener
) : AbsRequest(mOnResponseBodyTransformJsonListener),
    OnMobileRequestListener {

    override suspend fun get(
        rqParamModel: RqParamModel,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        rqParamModel.let {
            doRequest(
                rqParamModel,
                EmRequestType.GET,
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
                rqParamModel,
                EmSyncRequestType.GET_SYNC,
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
                rqParamModel,
                EmRequestType.POST,
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
                rqParamModel,
                EmSyncRequestType.POST_SYNC,
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
                rqParamModel,
                EmRequestType.POST_FORM,
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
                rqParamModel,
                EmSyncRequestType.POST_SYNC_FORM,
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
                rqParamModel,
                EmRequestType.PUT,
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
                rqParamModel,
                EmSyncRequestType.PUT_SYNC,
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
                rqParamModel,
                EmRequestType.PUT_FORM,
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
                rqParamModel,
                EmSyncRequestType.PUT_SYNC_FORM,
                onRequestSuccessListener,
                onRequestFailListener
            )
        }
    }


}