package com.gxx.neworklibrary.error.impl

import android.util.Log
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

/**
 * @author gaoxiaoxiong
 * 这是自定义错误
 */
class CustomErrorHandler (override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "UnErrorHandler"
    override fun handleError(error: AbsApiException): Boolean {
        if (error is CustomErrorApiException){
            Log.e(TAG, error.errorMessage);
            return true
        }else{
            return false
        }
    }
}