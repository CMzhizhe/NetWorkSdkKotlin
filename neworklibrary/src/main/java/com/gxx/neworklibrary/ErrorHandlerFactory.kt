package com.gxx.neworklibrary

object ErrorHandlerFactory {
    private val errorHandlerChain: MutableList<ErrorHandler> by lazy {
        createErrorHandlerChain()
    }
    private fun createErrorHandlerChain(): MutableList<ErrorHandler> {
        return ErrorHandlerBuilder()
            .addErrorHandler(NetWorkErrorHandler())
            .addErrorHandler(LoginErrorHandler())
            .build()
    }
    fun chain(): MutableList<ErrorHandler> {
        return errorHandlerChain
    }
    fun firstChain(): ErrorHandler {
        return errorHandlerChain.first()
    }
}
