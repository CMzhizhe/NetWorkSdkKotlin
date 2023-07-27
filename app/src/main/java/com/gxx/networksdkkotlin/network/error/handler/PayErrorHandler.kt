package com.gxx.networksdkkotlin.network.error.handler

import android.util.Log
import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.networksdkkotlin.network.error.exception.PayApiException
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 错误handler处理
 **/
class PayErrorHandler(override var next: OnErrorHandler?=null) : OnErrorHandler {
    private val TAG = "PayErrorHandler"
    override  fun handleError(error: AbsApiException): Boolean {
        if (error is PayApiException){
            if(BuildConfig.DEBUG){
                Log.d(TAG, "${PayErrorHandler::class.simpleName}已处理异常");
            }
            return true
        }else{
            return false
        }
    }
}