package com.gxx.neworklibrary.error.factory


import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.inter.OnErrorHandler


class ErrorHandlerFactory {
    private val mErrorHandlers = mutableListOf<OnErrorHandler>()

    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 拿到自己处理错误的Handler
      **/
    fun getErrorHandlers():MutableList<OnErrorHandler>{
        return mErrorHandlers
    }

    /**
     * @date 创建时间: 2023/7/24
     * @auther gaoxiaoxiong
     * @description 网络的异常
     * @param e 错误异常
     **/
    fun netWorkException(e: Throwable): ExceptionHandle.ResponeThrowable {
        return ExceptionHandle().handleException(e = e)
    }

    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 错误Handler处理
      **/
    fun propaGateError(
        errorHandler: OnErrorHandler?,
        error: AbsApiException
    ){

        if (errorHandler == null || errorHandler.handleError(error)) {
             //错误已处理
        } else {
            propaGateError(errorHandler.next, error)
        }
    }

    /**
     * @date 创建时间: 2023/7/24
     * @auther gaoxiaoxiong
     * @description 添加错误处理的 ErrorHandler
     **/
    fun addErrorHandler(onErrorHandler: OnErrorHandler): ErrorHandlerFactory {
        mErrorHandlers.add(onErrorHandler)
        return this
    }


    fun init():ErrorHandlerFactory{
        mErrorHandlers.reduceRight { left, right ->
            left.apply {
                this.next = right
            }
        }
        return this
    }
}
