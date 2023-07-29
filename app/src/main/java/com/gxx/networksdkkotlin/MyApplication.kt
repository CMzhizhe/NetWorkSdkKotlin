package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.OkHttpManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        OkHttpManager
            .addAbsLaunchUrlReq(WanAndroidMAFRequest)
            .create()
    }
}