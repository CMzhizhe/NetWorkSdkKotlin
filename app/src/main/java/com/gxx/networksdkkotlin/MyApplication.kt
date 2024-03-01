package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.OkHttpManager

class MyApplication: Application() {
    private val TAG = "MyApplication"
    override fun onCreate() {
        super.onCreate()
        OkHttpManager.getInstance().bindApplication(
            this
        )
    }
}