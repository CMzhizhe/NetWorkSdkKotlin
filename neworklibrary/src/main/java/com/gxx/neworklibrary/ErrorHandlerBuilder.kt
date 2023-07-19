package com.gxx.neworklibrary

class ErrorHandlerBuilder {
    private val errorHandlerList: MutableList<ErrorHandler> = mutableListOf()

    fun addErrorHandler(errorHandler: ErrorHandler): ErrorHandlerBuilder {
        errorHandlerList.add(errorHandler)
        return this
    }

    fun build(): MutableList<ErrorHandler> {
        errorHandlerList.reduceRight { left, right ->
            left.apply { next = right }
        }
        return errorHandlerList
    }
}
