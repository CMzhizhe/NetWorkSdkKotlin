package com.gxx.neworklibrary.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.gxx.neworklibrary.OkHttpManager


class NetWorkUtil {
    companion object{
        /**
         * @date 创建时间: 2023/8/10
         * @auther gaoxiaoxiong
         * @description 判断网络是否连接
         **/
        fun isNetConnected(): Boolean {
            val connectivityManager = OkHttpManager.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val capabilities =
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                            return true
                        }
                    }
                } else {
                    try {
                        val activeNetworkInfo = connectivityManager.activeNetworkInfo
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    } catch (e: Exception) {
                       e.printStackTrace()
                    }
                }
            }
            return false
        }

    }
}