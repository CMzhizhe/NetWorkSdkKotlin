package com.gxx.networksdkkotlin.network.error.exception

import com.gxx.neworklibrary.error.exception.AbsApiException

/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 支付异常
 **/
class PayApiException (code: String, jsString: String="", errorMessage: String = "") :
    AbsApiException(code, jsString, errorMessage) {
}