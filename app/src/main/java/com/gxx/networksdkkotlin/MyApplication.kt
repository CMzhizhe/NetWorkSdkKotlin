package com.gxx.networksdkkotlin

import android.app.Application
import com.gxx.networksdkkotlin.network.MAFRequest

class MyApplication: Application() {
    companion object{
          private var mMAFRequest:MAFRequest? = null
          fun getMAFRequest(): MAFRequest {
              return mMAFRequest!!
          }
    }

    override fun onCreate() {
        super.onCreate()
        mMAFRequest = MAFRequest()
        mMAFRequest!!.init()
    }
}