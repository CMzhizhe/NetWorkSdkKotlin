package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest

class MyApplication: Application() {
    private val TAG = "MyApplication"
    override fun onCreate() {
        super.onCreate()
        WanAndroidMAFRequest.init(this)
    }
}