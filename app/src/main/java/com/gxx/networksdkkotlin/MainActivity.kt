package com.gxx.networksdkkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.gxx.neworklibrary.RequestHelper
import com.gxx.neworklibrary.RequestResult
import com.gxx.neworklibrary.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val apiService: ApiService by lazy { RetrofitClient.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch{
            launch {

            }
           
        }





    }
}