package com.gxx.neworklibrary.error.factory


import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.inter.OnErrorHandler
import com.gxx.neworklibrary.model.ErrorPart


object ErrorHandlerFactory {
    private val mErrorPartList = mutableListOf<ErrorPart>()
    private val mErrorHandlers = mutableListOf<OnErrorHandler>()

    private fun getErrorHandlers():MutableList<OnErrorHandler>{
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
     * @date 创建时间: 2023/7/24
     * @auther gaoxiaoxiong
     * @description 添加错误part
     **/
    fun addErrorHandler(errorPart: ErrorPart): ErrorHandlerFactory {
        mErrorPartList.add(errorPart)
        return this
    }

    fun build(): MutableList<OnErrorHandler> {
        mErrorPartList.reduceRight { left, right ->
            left.apply {
                this.onErrorHandler.next = right.onErrorHandler
                mErrorHandlers.add(this.onErrorHandler)
            }
        }
        return mErrorHandlers
    }



}
