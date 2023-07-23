package com.gxx.neworklibrary.exception

open class ApiException(val code: String, message: String) : Throwable(message) {}