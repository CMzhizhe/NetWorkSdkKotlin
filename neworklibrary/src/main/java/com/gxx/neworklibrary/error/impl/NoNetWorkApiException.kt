package com.gxx.neworklibrary.error.impl

import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.exception.AbsApiException

/**
 * @author gaoxiaoxiong
 * @date 创建时间: 2023/8/12/012
 * @description  无网络异常
 **/
class NoNetWorkApiException(code: String = Constant.ERROR_NO_NET_WORK.toString(), jsString: String="", errorMessage: String = "暂无网络，请检查网络") :
    AbsApiException(code, jsString, errorMessage) {
}