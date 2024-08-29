package com.gxx.neworklibrary.inter


import retrofit2.CallAdapter
import retrofit2.Converter

interface OnGsonFactoryListener {
    fun converterFactoryList(): List<Converter.Factory>

    fun callAdapterFactoryList(): List<CallAdapter.Factory>

}