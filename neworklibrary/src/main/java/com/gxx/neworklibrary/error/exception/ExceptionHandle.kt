package com.gxx.neworklibrary.error.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ExceptionHandle {
    fun handleException(e: Throwable): ResponeThrowable {
        val ex: ResponeThrowable
        return if (e is HttpException) {
            val httpException = e
            ex = if (httpException.code() == INTERNAL_SERVER_ERROR) { //服务器异常
                ResponeThrowable(e, ERROR.HTTP_ERROR_500.toString())
            } else {
                if (httpException.code() == BAD_REQUEST){//badRequest
                    ResponeThrowable(e,BAD_REQUEST.toString())
                }else{
                    ResponeThrowable(e, ERROR.HTTP_ERROR.toString())
                }
            }
            when (httpException.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE,BAD_REQUEST -> if (httpException.message != null) {
                    ex.message = httpException.message
                } else if (httpException.message() != null) {
                    ex.message = httpException.message()
                } else {
                    ex.message = "网络错误"
                }
                else -> if (httpException.message != null) {
                    ex.message = httpException.message
                } else if (httpException.message() != null) {
                    ex.message = httpException.message()
                } else {
                    ex.message = "网络错误"
                }
            }
            ex
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
            || e is JsonSyntaxException
        ) {
            ex = ResponeThrowable(e, ERROR.PARSE_ERROR.toString())
            ex.message = "解析错误"
            ex
        } else if (e is ConnectException) {
            ex = ResponeThrowable(e, ERROR.NETWORD_ERROR.toString())
            ex.message = "请检查网络"
            ex
        } else if (e is SSLHandshakeException) {
            ex = ResponeThrowable(e, ERROR.SSL_ERROR.toString())
            ex.message = "证书验证失败"
            ex
        } else if (e is ConnectTimeoutException) {
            ex = ResponeThrowable(e, ERROR.TIMEOUT_ERROR.toString())
            ex.message = "连接超时"
            ex
        } else if (e is SocketTimeoutException) {
            ex = ResponeThrowable(e, ERROR.TIMEOUT_ERROR.toString())
            ex.message = "连接超时"
            ex
        } else if (e is UnknownHostException) {
            ex = ResponeThrowable(e, ERROR.NOADDRESS_ERROR.toString())
            ex.message = "请检查网络"
            ex
        } else {
            ex = ResponeThrowable(e, ERROR.UNKNOWN.toString())
            ex.message = "未知错误，自定义解析错误"
            ex
        }
    }

    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误，自定义解析错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006

        /**
         * 请检查网络
         */
        const val NOADDRESS_ERROR = 1007

        /**
         * 服务器错误
         */
        const val HTTP_ERROR_500 = 500
    }

    class ResponeThrowable(throwable: Throwable?, var code: String) : Exception(throwable) {
        override var message: String? = null
    }

    companion object {
        private const val BAD_REQUEST = 400//服务器错误
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504
    }
}