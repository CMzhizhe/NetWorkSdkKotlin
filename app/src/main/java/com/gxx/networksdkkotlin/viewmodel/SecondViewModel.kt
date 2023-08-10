package com.gxx.networksdkkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gxx.networksdkkotlin.bean.BaseBean
import com.gxx.networksdkkotlin.network.WanAndroidMAFRequest
import com.gxx.networksdkkotlin.network.pase.DataParseSuFaCall
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.bean.Banner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SecondViewModel: ViewModel()  {
    private val TAG = "SecondViewModel"
    val mBannerFlow = MutableSharedFlow<String>()

    /**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 发起网络请求
     **/
   suspend fun readBanner():MutableList<Banner>{
       return suspendCoroutine<MutableList<Banner>> {continuation ->
           viewModelScope.launch {
               WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                   DataParseSuFaCall<MutableList<Banner>>() {
                   override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                       super.onRequestDataSuccess(data)
                       continuation.resume(data!!)
                   }
                   override fun onRequestBaseBeanFail(baseBean: BaseBean?) {
                       super.onRequestBaseBeanFail(baseBean)
                   }
               })
           }
        }
    }



   /*
   https://www.jianshu.com/p/8f5149a66300
   https://www.jianshu.com/p/de4f8c1afaf6
   fun main() {
        // 开启一个主线程作用域的协程
        CoroutineScope(Dispatchers.Main).launch {
            // getUserInfo 是一个 suspend 函数，且在 IO 线程中
            val userInfo = getUserInfo()
            // 网络请求以同步的方式返回了，又回到了主线程，这里操作了 UI
            tvName.text = userInfo.name
        }
    }

    // withContext 运行在 IO 线程
    suspend fun getUserInfo() = withContext(Dispatchers.IO){
        // 这里的网络请求结果在 callback 中
        // 所以借助 suspendCoroutine 函数同步使用 callback 返回值
        // 返回值为 UserInfo 对象
        // 还有 suspendCancellableCoroutine 函数也可以了解下
        suspendCoroutine<UserInfo> {
            // 发起网络请求
            HttpRequest().url("/test/getUserInfo").callback(object : HttpCallback<UserInfo?>() {
                override fun onError(request: Request?, throwable: Throwable) {
                    it.resumeWithException(throwable)
                }

                override fun onResponse(userInfo: UserInfo?) {
                    it.resume(userInfo)
                }
            })
        }
    }*/
}