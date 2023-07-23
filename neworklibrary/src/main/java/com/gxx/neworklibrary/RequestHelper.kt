package com.gxx.neworklibrary

import com.gxx.neworklibrary.exception.NetworkException
import com.gxx.neworklibrary.exception.TimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object RequestHelper {
    suspend fun <T> request(call: suspend () -> Response<T>): Flow<RequestResult<Response<T>>> {
        return flow<RequestResult<Response<T>>> {
            val response: Response<T> = call.invoke()
            if (response.isSuccess()) {
                emit(RequestResult.Success(response))
            } else {
                when (response.getCode()) {
                    // 根据具体的错误码抛出对应的异常
                    -1 -> throw NetworkException(response.getCode(), response.getMessage())
                    -2 -> throw TimeoutException(response.getCode(), response.getMessage())
                   // else -> throw AbsApiException(response.getCode(), response.getMessage())
                }
            }
        }.catch { throwable: Throwable ->
            emit(RequestResult.Error(throwable))
        }.flowOn(Dispatchers.IO)

    }
}
