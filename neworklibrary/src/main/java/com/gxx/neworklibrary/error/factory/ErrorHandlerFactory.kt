package com.gxx.neworklibrary.error.factory


import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.inter.OnErrorHandler


class ErrorHandlerFactory {
    private var mOnErrorHandleFinishListener: OnErrorHandleFinishListener? = null
    private val mErrorHandlers = mutableListOf<OnErrorHandler>()
    private var mBaseUrl = ""

   /**
     * @date 创建时间: 2023/8/10
     * @auther gxx
     * 错误处理完成回调
     **/
    public interface OnErrorHandleFinishListener {
        fun onErrorHandleFinish()
    }

    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 拿到自己处理错误的Handler
     **/
    fun getErrorHandlers(): MutableList<OnErrorHandler> {
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
    ) {
        if (errorHandler == null || errorHandler.handleError(error)) {
            //错误已处理
            mOnErrorHandleFinishListener?.onErrorHandleFinish()
        } else {
            rollGateError(errorHandler.next, error)
        }
    }


    constructor(builder: Builder) {
        this.mBaseUrl = builder.getBaseUrl()
        this.mErrorHandlers.addAll(builder.getOnErrorHandlerList())
        this.mOnErrorHandleFinishListener = builder.getOnErrorHandleFinishListener()
        mErrorHandlers.reduceRight { left, right ->
            left.apply {
                this.next = right
            }
        }
    }

    class Builder {
        private val mErrorHandlers = mutableListOf<OnErrorHandler>()
        private var mBaseUrl = ""//baseUrl
        private var mOnErrorHandleFinishListener: OnErrorHandleFinishListener? = null//异常完成处理回调

        fun getOnErrorHandleFinishListener(): OnErrorHandleFinishListener? {
            return mOnErrorHandleFinishListener
        }

        fun getOnErrorHandlerList(): MutableList<OnErrorHandler> {
            return mErrorHandlers
        }

        fun getBaseUrl(): String {
            return mBaseUrl
        }

        fun setOnErrorHandleFinishListener(listener: OnErrorHandleFinishListener): Builder {
            this.mOnErrorHandleFinishListener = listener
            return this
        }



        /**
         * @date 创建时间: 2023/8/8
         * @auther gxx
         * 添加错误处理的OnErrorHandler
         **/
        fun addErrorHandler(onErrorHandler: OnErrorHandler): Builder {
            mErrorHandlers.add(onErrorHandler)
            return this
        }

        fun setBaseUrl(baseUrl:String):Builder{
            this.mBaseUrl = baseUrl
            return this
        }

        fun build(): ErrorHandlerFactory {
            if (this.mBaseUrl.isEmpty()) {
                throw IllegalStateException("请先设置 BaseUrl")
            }
            return ErrorHandlerFactory(this)
        }
    }
}
