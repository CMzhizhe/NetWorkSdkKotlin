package com.gxx.neworklibrary.error.impl.handler

import android.util.Log
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.impl.ParamsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class ParamsHandler (override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "ParamsError"
    override fun handleError(error: AbsApiException): Boolean {
        if (error is ParamsApiException){
            Log.e(TAG, error.errorMessage);
            return true
        }else{
            return false
        }
    }
}