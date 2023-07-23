package com.gxx.neworklibrary

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.gxx.neworklibrary.exception.LoginException
import com.gxx.neworklibrary.exception.NetworkException


interface ErrorHandler {
    var next: ErrorHandler?
    suspend fun handleError(error: ApiException, result: RequestResult.Error): Boolean
}


/*class LoginErrorHandler(override var next: ErrorHandler? = null) : ErrorHandler {
    override suspend fun handleError(error: ApiException, result: RequestResult.Error): Boolean {
        return if (error is LoginException) {
            // 处理需要登录的情况，例如跳转到登录页面或显示提示信息
            Log.d("LoginErrorHandler", "处理登陆异常,拦截")
            true
        } else {
            false
        }
    }
}*/
/*
class NetWorkErrorHandler(override var next: ErrorHandler? = null) : ErrorHandler {
    override suspend fun handleError(error: ApiException, result: RequestResult.Error): Boolean {
        return if (error is NetworkException) {
            if (error.code == 400) {
                Log.d("NetWorkErrorHandler", "客户端请求错误${error.code},不拦截")
            } else {
                Log.d("NetWorkErrorHandler", "处理网络错误${error.code},不拦截")
            }
            false
        } else {
            false
        }
    }
}
*/


