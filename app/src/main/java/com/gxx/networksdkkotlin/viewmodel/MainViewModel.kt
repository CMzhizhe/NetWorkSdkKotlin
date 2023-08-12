package com.gxx.networksdkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gxx.neworklibrary.model.BaseBean
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.neworklibrary.doservice.parse.BaseServiceDataParseSuFaCall
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner(){
        viewModelScope.launch{
            WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                BaseServiceDataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG){
                        Log.d(TAG, "json = ${Gson().toJson(data)}");
                    }
                }
                override fun onRequestBaseBeanFail(baseBean: BaseBean?) {
                    super.onRequestBaseBeanFail(baseBean)
                }
            })
        }
    }
}