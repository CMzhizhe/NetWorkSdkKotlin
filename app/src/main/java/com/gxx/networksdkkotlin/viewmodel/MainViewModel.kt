package com.gxx.networksdkkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gxx.networksdkkotlin.bean.BannerRequestModel
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.network.parse.ServiceDataParseCall
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

    private val  _bannerShareFlow = MutableSharedFlow<MutableList<Banner>>()
    val bannerShareFlow = _bannerShareFlow.asSharedFlow()

    /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner(){
        viewModelScope.launch {
            WanAndroidMAFRequest.postRequest(
                "banner/json",
                BannerRequestModel(
                    "123", mutableListOf("11","18")
                ),null,object :
                    ServiceDataParseCall<MutableList<Banner>>() {
                    override suspend fun onRequestBaseBeanSuccess(
                        data: MutableList<Banner>?,
                        baseBean: BaseBean
                    ) {
                        super.onRequestBaseBeanSuccess(data, baseBean)
                        if (data!=null){
                            _bannerShareFlow.emit(data!!)
                        }
                    }
                }
            )
        }
    }



    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  method方法返回
     **/
   suspend fun methodReturn():MutableList<Banner>{
        return suspendCoroutine {coroutine->
            viewModelScope.launch{
                WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                    ServiceDataParseCall<MutableList<Banner>>() {
                    override suspend fun onRequestBaseBeanSuccess(
                        data: MutableList<Banner>?,
                        baseBean: BaseBean
                    ) {
                        super.onRequestBaseBeanSuccess(data, baseBean)
                        if (data!=null){
                            coroutine.resume(data)
                        }
                    }
                })
            }
        }
    }


}