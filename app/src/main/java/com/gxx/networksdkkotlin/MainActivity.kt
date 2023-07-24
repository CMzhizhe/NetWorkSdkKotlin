package com.gxx.networksdkkotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
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
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG) {
                        Log.d(TAG, Gson().toJson(data));
                    }
                }
            })
        }
    }
}