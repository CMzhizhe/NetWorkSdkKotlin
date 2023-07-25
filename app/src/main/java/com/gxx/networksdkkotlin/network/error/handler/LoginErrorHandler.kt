package com.gxx.networksdkkotlin.network.error.handler

import android.util.Log
import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.networksdkkotlin.network.error.exception.LoginApiException
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 错误handler处理
 **/
class LoginErrorHandler(override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "LoginErrorHandler"
    override  fun handleError(error: AbsApiException): Boolean {
        if (error is LoginApiException){
            if(BuildConfig.DEBUG){
              Log.d(TAG, "${LoginErrorHandler::class.simpleName}已处理异常");
            }
            return true
        }else{
            return false
        }
    }
}