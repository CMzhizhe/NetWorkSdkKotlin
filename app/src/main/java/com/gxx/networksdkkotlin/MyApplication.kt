package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.MAFRequest

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MAFRequest.init()
    }
}