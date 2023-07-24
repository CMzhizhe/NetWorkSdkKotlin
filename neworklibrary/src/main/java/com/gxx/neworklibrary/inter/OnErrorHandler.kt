package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.error.exception.AbsApiException


interface OnErrorHandler {
    var next: OnErrorHandler?
    suspend fun handleError(error: AbsApiException): Boolean
}


