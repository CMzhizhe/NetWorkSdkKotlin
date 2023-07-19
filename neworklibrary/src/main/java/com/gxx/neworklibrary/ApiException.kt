package com.gxx.networksdkkotlin

open class ApiException(val code: Int, message: String) : Exception(message)

class NetworkException(code: Int, message: String) : ApiException(code, message)

class TimeoutException(code: Int, message: String) : ApiException(code, message)

class LoginException(code: Int, message: String) : ApiException(code, message)

