package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.OkHttpRequestManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        OkHttpRequestManager
            .addOkBuilder(WanAndroidMAFRequest.createOkBuilder())
            .create()
    }
}