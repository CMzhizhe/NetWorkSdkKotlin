package com.gxx.networksdkkotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gxx.networksdkkotlin.R
import com.gxx.networksdkkotlin.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val mMainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainViewModel.readBanner()
    }
}