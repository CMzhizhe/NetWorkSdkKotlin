package com.gxx.neworklibrary.error.impl.handler

import android.util.Log
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.impl.UnApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class UnErrorHandler (override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "UnErrorHandler"
    override fun handleError(error: AbsApiException): Boolean {
        if (error is UnApiException){
            Log.e(TAG, error.errorMessage);
            return true
        }else{
            return false
        }
    }
}