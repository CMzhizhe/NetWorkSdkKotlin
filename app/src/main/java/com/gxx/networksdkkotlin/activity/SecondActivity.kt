package com.gxx.networksdkkotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.networksdkkotlin.R
import com.gxx.networksdkkotlin.viewmodel.SecondViewModel
import kotlinx.coroutines.launch

class SecondActivity: AppCompatActivity()  {
    private val TAG = "SecondActivity"
    private val mSecondViewModel:SecondViewModel = SecondViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        subscribeBanner();
        lifecycleScope.launch{
           val result = mSecondViewModel.readBanner()
            if(BuildConfig.DEBUG) {
               Log.d(TAG, "result.first=${Gson().toJson(result.first())}");
            }
        }
    }

    private fun subscribeBanner(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.CREATED){
                mSecondViewModel.mBannerFlow.collect{
                    if(BuildConfig.DEBUG){
                      Log.d(TAG, "视图=${it}");
                    }
                }
            }
        }
    }
}