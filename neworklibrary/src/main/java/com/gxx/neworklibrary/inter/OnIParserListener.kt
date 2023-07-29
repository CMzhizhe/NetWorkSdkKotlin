package com.gxx.neworklibrary.inter

import com.google.gson.JsonElement

interface OnIParserListener {
    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 结果 data 数据页
     **/
    fun resultDataJsonElement():JsonElement?

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 原始jsonString数据
     **/
    fun sourceJsonString():String?

    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 是否成功，true 成功
     **/
    fun isSuccess():Boolean
}