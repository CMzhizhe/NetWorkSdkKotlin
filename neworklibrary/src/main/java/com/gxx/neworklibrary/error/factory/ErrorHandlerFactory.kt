package com.gxx.neworklibrary.error.factory


import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.inter.OnErrorHandler


class ErrorHandlerFactory {
    private var mOnErrorHandleFinishListener:OnErrorHandleFinishListener? = null
    private val mErrorHandlers = mutableListOf<OnErrorHandler>()

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 设置完成处理的回调
      **/
    fun setOnErrorHandleFinishListener(listener: OnErrorHandleFinishListener){
        this.mOnErrorHandleFinishListener = listener
    }

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 错误处理完成回调
      **/
    public interface OnErrorHandleFinishListener{
        fun onErrorHandleFinish()
    }

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
    fun rollGateError(
        errorHandler: OnErrorHandler?,
        error: AbsApiException
    ){

        if (errorHandler == null || errorHandler.handleError(error)) {
             //错误已处理
            mOnErrorHandleFinishListener?.onErrorHandleFinish()
        } else {
            rollGateError(errorHandler.next, error)
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
