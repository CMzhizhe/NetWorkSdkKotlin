package com.gxx.neworklibrary.model

import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class ErrorHandlerApiModel(val onErrorHandler: OnErrorHandler, val absApiException: AbsApiException) {
}