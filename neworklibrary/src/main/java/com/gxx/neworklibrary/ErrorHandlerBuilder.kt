package com.gxx.neworklibrary

import com.gxx.neworklibrary.inter.OnErrorHandler

class ErrorHandlerBuilder {
    private val onErrorHandlerList: MutableList<OnErrorHandler> = mutableListOf()

    fun addErrorHandler(onErrorHandler: OnErrorHandler): ErrorHandlerBuilder {
        onErrorHandlerList.add(onErrorHandler)
        return this
    }

    fun build(): MutableList<OnErrorHandler> {
        onErrorHandlerList.reduceRight { left, right ->
            left.apply { next = right }
        }
        return onErrorHandlerList
    }
}
