package com.gxx.networksdkkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gxx.networksdkkotlin.network.MAFRequest
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
            MAFRequest.getRequest("banner/json", mutableMapOf(),object :
                DataParseSuFaCall<MutableList<Banner>>() {

            })
        }
    }
}