package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.exception.ApiException

interface OnErrorHandlerListener {
    val next: OnErrorHandlerListener?
    fun handleError(exception: Throwable):Boolean
}