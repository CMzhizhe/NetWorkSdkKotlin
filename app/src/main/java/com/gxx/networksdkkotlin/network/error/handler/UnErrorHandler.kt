package com.gxx.networksdkkotlin.network.error.handler

import android.util.Log
import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.networksdkkotlin.network.error.exception.TokenApiException
import com.gxx.networksdkkotlin.network.error.exception.UnApiException
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class UnErrorHandler (override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "UnErrorHandler"
    override fun handleError(error: AbsApiException): Boolean {
        if (error is UnApiException){
            if(BuildConfig.DEBUG){
                Log.d(TAG, "${UnErrorHandler::class.simpleName}已处理异常");
            }
            return true
        }else{
            return false
        }
    }
}