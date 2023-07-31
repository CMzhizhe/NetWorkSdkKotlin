package com.gxx.testalibrary

import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.testalibrary.bean.Banner
import com.gxx.testalibrary.network.ANetWorkRequest
import com.gxx.testalibrary.network.pase.DataParseSuFaCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit

/**
 * @date 创建时间: 2023/7/31
 * @auther gxx
 * @description A 包管理工具类
 **/
class TestANetWorkManager(private val retrofit: Retrofit) {
    private val mCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val TAG = "TestANetWorkManager"

    init {
        OkHttpManager.addAbsLaunchUrlReq(ANetWorkRequest.setRetrofit(retrofit))
    }


    /**
      * @date 创建时间: 2023/7/31
      * @auther gxx
      * @description 初始化
      **/
    fun init() {
        readData()
    }


    /**
     * @date 创建时间: 2023/7/31
     * @auther gxx
     * @description 发起请求
     **/
   private fun readData() {
        mCoroutineScope.launch {
            delay(5 * 1000)
            ANetWorkRequest.getRequest("banner/json", mutableMapOf(), object :
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "json = ${Gson().toJson(data)}");
                        Log.d(
                            TAG,
                            "是否主线程 = ${Looper.getMainLooper() == Looper.myLooper()}"
                        );
                    }
                }
            })
        }
    }

}