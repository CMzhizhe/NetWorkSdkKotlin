package com.gxx.neworklibrary.inter

interface OnJavaRequestFailListener {
    fun onJavaRequestFail(
        method:String,
        exception: Throwable? = null,
        onIParserListener: OnIParserListener?=null
    )
}