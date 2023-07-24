package com.gxx.networksdkkotlin.network.error.handler

import com.gxx.networksdkkotlin.network.error.exception.LoginApiException
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 错误handler处理
 **/
class LoginErrorHandler(override var next: OnErrorHandler?) : OnErrorHandler {
    override suspend fun handleError(error: AbsApiException): Boolean {
        if (error is LoginApiException){

            return true
        }else{
            return false
        }
    }
}