package com.gxx.networksdkkotlin.network.error.handler

import com.gxx.networksdkkotlin.network.error.exception.TokenApiException
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class UnErrorHandler (override var next: OnErrorHandler?) : OnErrorHandler {
    override  fun handleError(error: AbsApiException): Boolean {
        if (error is TokenApiException){

            return true
        }else{
            return false
        }
    }
}