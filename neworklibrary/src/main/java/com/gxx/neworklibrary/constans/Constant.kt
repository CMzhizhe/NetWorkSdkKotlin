package com.gxx.neworklibrary.constans

class Constant {
    companion object{
        const val DEFAULT_PORT_80 = 80
        const val DEFAULT_PORT_443 = 443

        const val ERROR_ROOT_JSON_EMPTY = -97//rootJson是空的
        const val ERROR_NO_NET_WORK = -98//无网络
        const val ERROR_UN_ERROR = -99//没有定义错误类型
        const val ERROR_PARAMS = -100//参数错误

        const val APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8"
    }
}