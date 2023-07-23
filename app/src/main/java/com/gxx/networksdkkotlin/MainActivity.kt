package com.gxx.networksdkkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.networksdkkotlin.network.ServiceDataParse
import com.gxx.neworklibrary.RequestHelper
import com.gxx.neworklibrary.RequestResult
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch{
            // 使用ApiRequestController进行请求
            MyApplication.getMAFRequest().getRequest("banner/json", mutableMapOf(),object :
                ServiceDataParse<Banner>() {
                override fun onRequestDataSuccess(data: Banner?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG) {
                       Log.d(TAG, Gson().toJson(data));
                    }
                }
            })
        }
    }
}