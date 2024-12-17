package com.gxx.networksdkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.GsonUtils
import com.gxx.networksdkkotlin.BuildConfig
import com.gxx.networksdkkotlin.bean.BannerRequestModel
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.network.parse.ServiceDataParseCall
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

    private val  _bannerShareFlow = MutableSharedFlow<MutableList<Banner>>()
    val bannerShareFlow = _bannerShareFlow.asSharedFlow()

    private val _bannerStateFlow = MutableStateFlow<MutableList<Banner>>(mutableListOf())
    val bannerStateFlow = _bannerStateFlow.asStateFlow()

    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 发起网络请求
     **/
    fun readBannerV2() {
        viewModelScope.launch {
            WanAndroidMAFRequest.postRequestV2<MutableList<Banner>>(
                "banner/json",
                BannerRequestModel("123", mutableListOf("11", "18"),1,12.4f,1000.toDouble(),System.currentTimeMillis()),
                urlMap = null,
                success = { list, baseBean ->
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "list---->${list}");
                        Log.d(TAG, "baseBean---->${GsonUtils.toJson(baseBean)}");
                    }
                    if (list != null) {
                        _bannerStateFlow.value = list
                    }
                },
                fail = {

                })
        }
    }

    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner() {
        viewModelScope.launch {
            WanAndroidMAFRequest.postRequest<MutableList<Banner>>(
                "banner/json",
                BannerRequestModel("123", mutableListOf("11", "18"),1,12.4f,1000.toDouble(),System.currentTimeMillis()),
                urlMap = null,
                success = { list, baseBean ->
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "list---->${list}");
                        Log.d(TAG, "baseBean---->${GsonUtils.toJson(baseBean)}");
                    }
                    if (list != null) {
                        _bannerShareFlow.emit(list)
                    }
                },
                fail = {

                })
        }
    }


    fun readBannerV3() {
        viewModelScope.launch {
            WanAndroidMAFRequest.postRequest<MutableList<Banner>>(
                "banner/json",
                BannerRequestModel("123", mutableListOf("11", "18"),1,12.4f,1000.toDouble(),System.currentTimeMillis()),
                urlMap = null,
                success = { list, baseBean ->
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "list---->${list}");
                        Log.d(TAG, "baseBean---->${GsonUtils.toJson(baseBean)}");
                    }
                    if (list != null) {
                        _bannerShareFlow.emit(list)
                    }
                },
                fail = {

                })
        }
    }



    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  method方法返回
     **/
    suspend fun methodReturn(): MutableList<Banner> {
        return suspendCoroutine { coroutine ->
            viewModelScope.launch {
                WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(), object :
                    ServiceDataParseCall<MutableList<Banner>>() {
                    override suspend fun onRequestBaseBeanSuccess(
                        data: MutableList<Banner>?,
                        baseBean: BaseBean
                    ) {
                        super.onRequestBaseBeanSuccess(data, baseBean)
                        if (data != null) {
                            coroutine.resume(data)
                        }
                    }
                })
            }
        }
    }
}