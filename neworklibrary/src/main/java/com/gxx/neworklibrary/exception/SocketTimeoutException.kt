package com.gxx.neworklibrary.exception

import com.gxx.neworklibrary.inter.OnErrorHandlerListener

class SocketTimeoutException(override val next: OnErrorHandlerListener?) : OnErrorHandlerListener {
    override fun handleError(exception: Throwable): Boolean {
        return exception is java.net.SocketTimeoutException
    }
}