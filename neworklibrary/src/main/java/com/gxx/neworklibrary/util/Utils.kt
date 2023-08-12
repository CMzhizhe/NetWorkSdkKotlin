package com.gxx.neworklibrary.util

import android.net.Uri
import com.gxx.neworklibrary.constans.Constant

class Utils {

    companion object{
        /**
         * @author gaoxiaoxiong
         * @date 创建时间: 2023/8/12/012
         * @description  通过 method 获取到BaseUrl
         * @param method 域名 + 端口 + 接口名称
         **/
        fun getBaseUrlByMethod(method:String):String{
            val uri = Uri.parse(method)
            if (uri.port == Constant.DEFAULT_PORT_80 || uri.port == Constant.DEFAULT_PORT_443 || uri.port == -1){
                return "${uri.scheme}://${uri.host}/"
            }else{
                return "${uri.scheme}://${uri.host}:${uri.port}/"
            }
        }
    }

}