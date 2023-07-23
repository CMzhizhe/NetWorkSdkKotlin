package com.gxx.neworklibrary.model


import com.gxx.neworklibrary.constans.EmResultType

/**
 * @date 创建时间: 2023/7/23
 * @auther gaoxiaoxiong
 * @description 请求参数封装类型
 * @param method 请求的接口名称
 * @param bodyMap put 和 post 需要的参数
 * @param urlMap 拼接在url后面的参数
 * @param emResultType 对想要的服务器结果提供的类型     自己处理类型   只返回对象  只返回数组
 **/
class RqParamModel(
    var method: String = "",
    var bodyMap: Map<String, Any>? = null,
    var urlMap: Map<String, Any>? = null,
    var emResultType: EmResultType = EmResultType.REQUEST_RESULT_OWN
)