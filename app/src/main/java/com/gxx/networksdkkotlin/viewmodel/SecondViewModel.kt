package com.gxx.networksdkkotlin.viewmodel

import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SecondViewModel: ViewModel()  {
    private val TAG = "SecondViewModel"
    val mBannerFlow = MutableSharedFlow<String>()

    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 发起网络请求
     **/
    fun readBanner(){
        viewModelScope.launch{
            WanAndroidMAFRequest.createRequestFlow<MutableList<Banner>?>("banner/json").collect{
                mBannerFlow.emit(Gson().toJson(it))
            }
        }
    }
}