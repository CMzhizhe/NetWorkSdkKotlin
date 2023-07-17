package com.gxx.neworklibrary

data class Response<T>(
    private val errorCode: Int? = -9999,
    private val errorMsg: String? = "",
    private val data: T? = null
) {
    fun isSuccess(): Boolean {
        return errorCode == 0
    }

    fun isFailure(): Boolean {
        return !isSuccess()
    }

    fun getCode(): Int {
        return errorCode ?: -9999
    }

    fun getData(): T? {
        return data
    }

    fun getMessage(): String {
        return errorMsg ?: ""
    }
}
