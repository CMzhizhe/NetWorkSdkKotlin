package com.gxx.neworklibrary.inter

import com.google.gson.JsonElement

interface OnJavaRequestSuccessListener {
    fun onJavaRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    )
}