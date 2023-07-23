package com.gxx.neworklibrary.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.gxx.neworklibrary.inter.OnErrorHandlerListener
import org.json.JSONException

class JsonException(override val next: OnErrorHandlerListener?) : OnErrorHandlerListener {
    override fun handleError(exception: Throwable): Boolean {
        return exception is JsonParseException || exception is JSONException || exception is ParseException || exception is JsonSyntaxException
    }
}