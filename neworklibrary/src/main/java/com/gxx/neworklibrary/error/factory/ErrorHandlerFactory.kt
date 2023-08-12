package com.gxx.neworklibrary.error.factory


import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.error.impl.*
import com.gxx.neworklibrary.inter.OnErrorHandler
import com.gxx.neworklibrary.model.ErrorHandlerApiModel


class ErrorHandlerFactory {
    private var mOnServiceCodeErrorHandleFinishListener: OnServiceCodeErrorHandleFinishListener? = null//服务器错误回调
    private var mOnNetWorkErrorListener:OnNetWorkErrorListener? = null//网络错误调用

    private val mServiceErrorHandlers = mutableListOf<OnErrorHandler>()//服务器的serviceHandler
    private var mServiceErrorApiExceptions = mutableListOf<AbsApiException>()//服务器的errorApi
    private var mBaseUrl = ""

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  网络错误回调
     **/
    interface OnNetWorkErrorListener{
        fun onNetWorkError(throwable:ExceptionHandle.ResponeThrowable )
    }

   /**
     * @date 创建时间: 2023/8/10
     * @auther gxx
     * 错误处理完成回调
     **/
    public interface OnServiceCodeErrorHandleFinishListener {
        fun onServiceCodeErrorHandleFinish(error: AbsApiException)
    }



    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 拿到自己处理错误的Handler
     **/
    fun getServiceErrorHandlers(): MutableList<OnErrorHandler> {
        return mServiceErrorHandlers
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  拿到异常
     **/
    fun getServiceErrorApiExceptions():MutableList<AbsApiException>{
        return mServiceErrorApiExceptions
    }

    /**
     * @date 创建时间: 2023/7/24
     * @auther gaoxiaoxiong
     * @description 网络的异常
     * @param e 错误异常
     **/
    fun netWorkException(e: Throwable) {
        val responeThrowable = ExceptionHandle().handleException(e = e)
        mOnNetWorkErrorListener?.onNetWorkError(responeThrowable)
    }

    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * 与服务器定义的code错误回调
     **/
    fun rollServiceCodeGateError(
        errorHandler: OnErrorHandler?,
        error: AbsApiException
    ) {
        if (errorHandler == null || errorHandler.handleError(error)) {
            //错误已处理
            mOnServiceCodeErrorHandleFinishListener?.onServiceCodeErrorHandleFinish(error)
        } else {
            rollServiceCodeGateError(errorHandler.next, error)
        }
    }


    constructor(builder: Builder) {
        this.mBaseUrl = builder.getBaseUrl()
        this.mOnServiceCodeErrorHandleFinishListener = builder.getOnServiceCodeErrorHandleFinishListener()
        this.mOnNetWorkErrorListener = builder.getOnNetWorkErrorListener()

        //添加默认handler
        if (builder.getIsAddDefaultServiceErrorHandle()){
            for (defaultErrorHandlerAndApiException in defaultErrorHandlerAndApiExceptions()) {
                mServiceErrorHandlers.add(defaultErrorHandlerAndApiException.onErrorHandler)
                mServiceErrorApiExceptions.add(defaultErrorHandlerAndApiException.absApiException)
            }
        }

        //添加用户额外补充的错误
        for (serviceErrorModel in builder.getServiceErrorModel()) {
            mServiceErrorHandlers.add(serviceErrorModel.onErrorHandler)
            mServiceErrorApiExceptions.add(serviceErrorModel.absApiException)
        }

        //添加无网络 + 未定义异常
        mServiceErrorHandlers.add(NoNetErrorHandler())
        mServiceErrorHandlers.add(UnErrorHandler())

        mServiceErrorHandlers.reduceRight { left, right ->
            left.apply {
                this.next = right
            }
        }
    }

    class Builder {
        private val mErrorHandlerApiModels = mutableListOf<ErrorHandlerApiModel>()
        private var mBaseUrl = ""//baseUrl
        private var mIsAddDefaultServiceErrorHandle = true//是否service提供的默认的错误
        private var mOnServiceCodeErrorHandleFinishListener: OnServiceCodeErrorHandleFinishListener? = null//异常完成处理回调
        private var mOnNetWorkErrorListener:OnNetWorkErrorListener? = null

        fun getOnNetWorkErrorListener():OnNetWorkErrorListener?{
            return mOnNetWorkErrorListener
        }

        fun getIsAddDefaultServiceErrorHandle():Boolean{
            return mIsAddDefaultServiceErrorHandle
        }
        
        fun setIsAddDefaultServiceErrorHandle(boolean: Boolean):Builder{
            this.mIsAddDefaultServiceErrorHandle = boolean
            return this
        }

        fun getOnServiceCodeErrorHandleFinishListener(): OnServiceCodeErrorHandleFinishListener? {
            return mOnServiceCodeErrorHandleFinishListener
        }

        fun getServiceErrorModel(): MutableList<ErrorHandlerApiModel> {
            return mErrorHandlerApiModels
        }

        fun getBaseUrl(): String {
            return mBaseUrl
        }

        fun setOnNetWorkErrorListener(listener:OnNetWorkErrorListener):Builder{
            this.mOnNetWorkErrorListener = listener
            return this
        }

        fun setOnServiceCodeErrorHandleFinishListener(listener: OnServiceCodeErrorHandleFinishListener): Builder {
            this.mOnServiceCodeErrorHandleFinishListener = listener
            return this
        }
        
        /**
         * @date 创建时间: 2023/8/8
         * @auther gxx
         * 添加错误处理的OnErrorHandler
         **/
        fun addErrorHandler(onErrorHandler: OnErrorHandler,absApiException: AbsApiException): Builder {
            mErrorHandlerApiModels.add(ErrorHandlerApiModel(onErrorHandler, absApiException))
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

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  默认的错误
     **/
    fun defaultErrorHandlerAndApiExceptions():MutableList<ErrorHandlerApiModel>{
        val list = mutableListOf<ErrorHandlerApiModel>()
        list.apply {
            add(ErrorHandlerApiModel(RootJsonEmptyErrorHandler(),RootJsonEmptyApiException()))
        }
        return list
    }


}
