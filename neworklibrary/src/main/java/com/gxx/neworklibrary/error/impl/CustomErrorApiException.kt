package com.gxx.neworklibrary.error.impl

import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.exception.AbsApiException

class CustomErrorApiException (code: String = Constant.ERROR_CUSTOMER_ERROR_1001.toString(), jsString: String="", errorMessage: String = "这是自定义错误") :
    AbsApiException(code, jsString, errorMessage) {
}