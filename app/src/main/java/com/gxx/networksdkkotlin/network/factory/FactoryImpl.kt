package com.gxx.networksdkkotlin.network.factory


import com.gxx.neworklibrary.inter.OnFactoryListener
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

class FactoryImpl: OnFactoryListener {
    override fun converterFactorys(): List<Converter.Factory> {
        val list = mutableListOf<Converter.Factory>()
        list.add(GsonConverterFactory.create())
        return list
    }

    override fun callAdapterFactorys(): List<CallAdapter.Factory> {
        val list = mutableListOf<CallAdapter.Factory>()
        return list
    }
}