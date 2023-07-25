package com.gxx.networksdkkotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gxx.networksdkkotlin.R
import com.gxx.networksdkkotlin.network.MAFRequest
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
import com.gxx.networksdkkotlin.viewmodel.MainViewModel
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val mMainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainViewModel.readBanner()
    }
}