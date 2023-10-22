package com.gxx.neworklibrary.model

import com.google.gson.JsonElement
import com.gxx.neworklibrary.base.doservicedata.transform.BaseServiceDataTransform
import com.gxx.neworklibrary.inter.OnIParserListener

class BaseBean(var method: String? = null,
               var resourceJsonString: String? = null,
               var jsonElement: JsonElement? = null,
               var errorCode:String,msg:String="") : OnIParserListener {
    override fun resultDataJsonElement(): JsonElement? {
        return jsonElement
    }

    override fun sourceJsonString(): String? {
        return resourceJsonString;
    }

    override fun isSuccess(): Boolean {
        return errorCode == BaseServiceDataTransform.ERROR_CODE_TYPE_0.toString()
    }
}