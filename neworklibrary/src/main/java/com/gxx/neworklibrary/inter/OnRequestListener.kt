package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.model.RqParamModel


interface OnRequestListener {

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description
     * @param rqParamModel 请求参数model封装
     * @param emRequestType 请求类型
     * @param  requestResultValue 希望服务器给的集合的结果
     */
   suspend fun doRequest(
        rqParamModel: RqParamModel,
        emRequestType: EmRequestType,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    )


}