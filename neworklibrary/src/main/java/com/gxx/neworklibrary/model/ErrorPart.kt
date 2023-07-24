package com.gxx.neworklibrary.model

import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class ErrorPart(val absApiException :AbsApiException,val onErrorHandler :OnErrorHandler) {

}