package com.gxx.networksdkkotlin

import com.gxx.neworklibrary.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApiRequestRepository {
    suspend fun <T> process(call: suspend () -> Response<T>): Flow<RequestResult<Response<T>>> {
        return RequestHelper.request(call).map { result ->
            when (result) {
                is RequestResult.Success -> result
                is RequestResult.Error -> {
                    val error = result.error
                    if (error is ApiException) {
                        propagateError(ErrorHandlerFactory.firstChain(), error, result)
                    } else {
                        result
                    }
                }
            }
        }
    }
    private suspend fun propagateError(
        errorHandler: ErrorHandler?,
        error: ApiException,
        result: RequestResult.Error
    ): RequestResult.Error {
        return if (errorHandler == null || errorHandler.handleError(error, result)) {
            result
        } else {
            propagateError(errorHandler.next, error, result)
        }
    }
}
