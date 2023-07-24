package com.gxx.networksdkkotlin.network.error

import com.gxx.neworklibrary.RequestResult
import com.gxx.neworklibrary.exception.ApiException
import com.gxx.neworklibrary.inter.OnErrorHandler

class LoginErrorHandler(override var next: OnErrorHandler?) : OnErrorHandler {
    override suspend fun handleError(error: ApiException, result: RequestResult.Error): Boolean {
        TODO("Not yet implemented")
    }
}