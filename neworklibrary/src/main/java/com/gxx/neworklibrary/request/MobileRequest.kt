package com.gxx.neworklibrary.request

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.doservice.transform.BaseServiceDataTransform
import com.gxx.neworklibrary.inter.OnMobileRequestListener
import com.gxx.neworklibrary.inter.OnRequestFailListener
import com.gxx.neworklibrary.inter.OnRequestSuccessListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.base.AbsRequest

/**
 * @date 创建时间: 2023/7/31
 * @auther gaoxiaoxiong
 * @description 网络请求封装
 * @param mOnResponseBodyTransformJsonListener 服务器给的参数，回传给调用者
 **/
open class MobileRequest(
    mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener?=null
) : AbsRequest(mOnResponseBodyTransformJsonListener ?: BaseServiceDataTransform()),
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

}