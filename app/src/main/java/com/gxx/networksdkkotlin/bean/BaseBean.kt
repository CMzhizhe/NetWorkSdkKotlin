package com.gxx.networksdkkotlin.bean

import com.google.gson.JsonElement
import com.gxx.neworklibrary.inter.OnIParserListener

class BaseBean(var method: String? = null,
               var resourceJsonString: String? = null,
               var jsonElement: JsonElement? = null,
               var errorCode:Int) : OnIParserListener {
    override fun resultDataJsonElement(): JsonElement? {
        return jsonElement
    }

    override fun sourceJsonString(): String? {
        return resourceJsonString;
    }

    override fun isSuccess(): Boolean {
        return errorCode == 0
    }
}