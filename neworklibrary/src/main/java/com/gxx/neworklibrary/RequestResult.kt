package com.gxx.neworklibrary

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val error: Throwable) : RequestResult<Nothing>()
}
