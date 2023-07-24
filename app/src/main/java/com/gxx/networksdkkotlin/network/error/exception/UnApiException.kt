package com.gxx.networksdkkotlin.network.error.exception

import com.gxx.neworklibrary.error.exception.AbsApiException

class UnApiException(code: String, jsString: String, errorMessage: String = "") :
    AbsApiException(code, jsString, errorMessage) {
}