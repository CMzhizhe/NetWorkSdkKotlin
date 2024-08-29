package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.util.AssetsHttpConfigRead
import com.gxx.neworklibrary.OkHttpManager

class MyApplication: Application() {
    private val TAG = "MyApplication"

    companion object{
        const val HTTP_NAME = "http_config.json"
    }

    override fun onCreate() {
        super.onCreate()
        WanAndroidMAFRequest.init(this)

    }
}