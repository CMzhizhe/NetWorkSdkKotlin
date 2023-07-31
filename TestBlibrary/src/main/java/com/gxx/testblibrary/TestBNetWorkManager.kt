package com.gxx.testblibrary

import com.gxx.neworklibrary.OkHttpManager
import com.gxx.testblibrary.network.BNetWorkRequest

class TestBNetWorkManager {
    init {
        OkHttpManager
            .addAbsLaunchUrlReq(BNetWorkRequest)
            .create()
    }
}