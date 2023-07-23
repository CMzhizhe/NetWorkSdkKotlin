package com.gxx.neworklibrary.inter



interface OnErrorHandlerListener {
    val next: OnErrorHandlerListener?
    fun handleError(exception: Throwable):Boolean
}