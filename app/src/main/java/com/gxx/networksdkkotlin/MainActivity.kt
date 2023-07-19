package com.gxx.networksdkkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.gxx.neworklibrary.RequestHelper
import com.gxx.neworklibrary.RequestResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val apiService: ApiService by lazy { RetrofitClient.create() }
    private val apiRequestRepository = ApiRequestRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch{
            // 使用ApiRequestController进行请求
            apiRequestRepository.process { apiService.getBanners() }.collect { result ->
                when (result) {
                    is RequestResult.Success -> {
                        if(BuildConfig.DEBUG) {
                            Log.d(TAG, "是否在主线程=${Looper.getMainLooper() == Looper.myLooper()}")
                            Log.d(TAG, gson.toJson(result.data.getData()))
                        }
                    }

                    is RequestResult.Error->{

                    }
                }
            }
        }
    }
}