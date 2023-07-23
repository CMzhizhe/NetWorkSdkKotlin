package com.gxx.neworklibrary.inter

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 将 ResopneBody 转成 需要的json格式
 **/
interface OnResponseBodyTransformJsonListener {
    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description
     * @param method 请求的方法名称
     * @param jsString 服务器拿到的结果集
     **/
    fun onResponseBodyTransformJson(method:String,jsString: String):OnIParserListener
}