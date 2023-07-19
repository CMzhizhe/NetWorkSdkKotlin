package com.gxx.networksdkkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch{
            RequestHelper.request {
                apiService.getBanners()
            }.collect {
                when (it) {
                    is RequestResult.Success -> {
                        if(BuildConfig.DEBUG) {
                           Log.d(TAG, gson.toJson(it.data.getData()));
                        }
                    }
                    is RequestResult.Error -> {

                    }
                }

            }

        }
    }
}