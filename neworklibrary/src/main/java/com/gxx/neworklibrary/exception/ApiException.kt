package com.gxx.neworklibrary.exception

open class ApiException(val code: Int, message: String) : Throwable(message) {}