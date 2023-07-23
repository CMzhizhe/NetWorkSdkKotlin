package com.gxx.neworklibrary.exception

import com.gxx.neworklibrary.inter.OnErrorHandlerListener
import org.apache.http.conn.ConnectTimeoutException

class ConnectTimeOutException(override val next: OnErrorHandlerListener?) : OnErrorHandlerListener {
    override fun handleError(exception: Throwable): Boolean {
         return exception is ConnectTimeoutException
    }
}