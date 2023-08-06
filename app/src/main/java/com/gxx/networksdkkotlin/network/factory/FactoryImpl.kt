package com.gxx.networksdkkotlin.network.factory


import com.gxx.neworklibrary.inter.OnFactoryListener
import retrofit2.CallAdapter
import retrofit2.Converter

class FactoryImpl: OnFactoryListener {
    override fun converterFactorys(): List<Converter.Factory> {
        return  mutableListOf<Converter.Factory>()
    }

    override fun callAdapterFactorys(): List<CallAdapter.Factory> {
        return mutableListOf<CallAdapter.Factory>()
    }
}