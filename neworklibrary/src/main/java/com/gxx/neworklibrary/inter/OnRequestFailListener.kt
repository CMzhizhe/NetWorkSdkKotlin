package com.gxx.neworklibrary.inter

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 失败调用
 **/
interface OnRequestFailListener {
   suspend fun onRequestFail(
        method:String,
        throwable: Throwable?=null,
        status: String?=null,
        failMsg: String?=null,
        errorJsonString: String?=null,
        onIParserListener: OnIParserListener?=null
    )
}