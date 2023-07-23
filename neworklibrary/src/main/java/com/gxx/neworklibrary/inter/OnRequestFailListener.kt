package com.gxx.neworklibrary.inter

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 失败调用
 **/
interface OnRequestFailListener {
    fun onRequestFail(
        throwable: Throwable?,
        status: String?,
        failMsg: String?,
        errorJsonString: String?,
        onIParserListener: OnIParserListener?
    )
}