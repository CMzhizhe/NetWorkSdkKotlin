package com.gxx.neworklibrary.exception


abstract class AbsApiException(val code: Int, message: String) : Exception(message){

}

class NetworkException(code: Int, message: String) : AbsApiException(code, message)

class TimeoutException(code: Int, message: String) : AbsApiException(code, message)

class LoginException(code: Int, message: String) : AbsApiException(code, message)

