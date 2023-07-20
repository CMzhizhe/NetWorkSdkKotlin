package com.gxx.neworklibrary.inter

import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.Converter

interface OnFactoryListener {
    fun converterFactorys(): List<Converter.Factory>

    fun callAdapterFactorys(): List<CallAdapter.Factory>

}