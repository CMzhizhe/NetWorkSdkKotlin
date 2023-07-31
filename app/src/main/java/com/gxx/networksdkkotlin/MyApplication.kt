package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.testalibrary.TestANetWorkManager
import com.gxx.testblibrary.TestBNetWorkManager

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        OkHttpManager
            .addAbsLaunchUrlReq(WanAndroidMAFRequest)
            .create()
        //构建相同的B域名
        TestBNetWorkManager()
        //拿到构建好的 Retrofit传递进去
        TestANetWorkManager().init()
    }
}