package com.gxx.neworklibrary.error.exception

/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description
 * @param code 错误code
 * @param jsString 错误json
 * @param errorMessage 错误message
 **/
abstract class AbsApiException(val code: String, var jsString: String, var errorMessage:String=""):Exception(errorMessage) {}