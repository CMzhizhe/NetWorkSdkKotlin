package com.gxx.networksdkkotlin.viewmodel

import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG,"$throwable")
    }


    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner(){
        viewModelScope.launch{
            WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG){
                        Log.d(TAG, "json = ${Gson().toJson(data)}");
                        Log.d(TAG, "是否主线程 = ${Looper.getMainLooper() == Looper.myLooper()}");
                    }
                }
            })
        }
    }
}