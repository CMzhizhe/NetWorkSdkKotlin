package com.gxx.neworklibrary.error.impl

import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.exception.AbsApiException

/**
 * @author gaoxiaoxiong
 * @date 创建时间: 2023/8/12/012
 * @description  rootJson是空的
 **/
class RootJsonEmptyApiException(code: String = Constant.ERROR_ROOT_JSON_EMPTY.toString(), jsString: String="", errorMessage: String = "rootJson是空的") :
    AbsApiException(code, jsString, errorMessage) {
}