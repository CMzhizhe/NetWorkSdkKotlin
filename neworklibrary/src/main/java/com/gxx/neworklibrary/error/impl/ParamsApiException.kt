package com.gxx.neworklibrary.error.impl

import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.exception.AbsApiException

class ParamsApiException (code: String = Constant.ERROR_PARAMS.toString(), jsString: String="", errorMessage: String = "参数传递有误") :
    AbsApiException(code, jsString, errorMessage) {
}