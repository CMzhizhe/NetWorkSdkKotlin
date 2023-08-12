package com.gxx.neworklibrary.error.impl

import com.gxx.neworklibrary.constans.Constant.Companion.ERROR_UN_ERROR
import com.gxx.neworklibrary.error.exception.AbsApiException

class UnApiException(code: String = ERROR_UN_ERROR.toString(), jsString: String="", errorMessage: String = "没有与服务器定义错误类型") :
    AbsApiException(code, jsString, errorMessage) {
}