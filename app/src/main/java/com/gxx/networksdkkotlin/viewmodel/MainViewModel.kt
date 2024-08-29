package com.gxx.networksdkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gxx.networksdkkotlin.bean.BannerRequestModel
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.network.parse.ServiceDataParseCall
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

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
                    override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                        super.onRequestDataSuccess(data)

                    }
                }
            )

           /* WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                BaseServiceDataParseCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)

                }
            })*/
        }


        /*viewModelScope.launch{
            WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                BaseServiceDataParseCall<MutableList<Banner>>() {
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
        }*/
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description  flow方式请求
     **/
    fun readBannerFlow(){
        viewModelScope.launch {
            WanAndroidMAFRequest.createRequestFlow<MutableList<Banner>>("banner/json").collect{
                if(BuildConfig.DEBUG){
                 Log.d(TAG, "flow拿到的结果->${Gson().toJson(it)}");
                }
            }
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
                    override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                        super.onRequestDataSuccess(data)
                        if (data!=null){
                            coroutine.resume(data)
                        }
                    }
                })
            }
        }
    }


}