package com.gxx.neworklibrary.inter

import com.google.gson.JsonElement

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 成功调用
 **/
interface OnRequestSuccessListener {
    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description
     * @param method 方法名
     * @param targetElement  data 里面的参数
     **/
    fun onRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    )
}