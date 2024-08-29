package com.gxx.neworklibrary.util

import android.net.Uri
import com.gxx.neworklibrary.constans.Constant
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


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

        /**
         * @author gaoxiaoxiong
         * 检查是否为空的
         */
         fun checkUrlIsEmpty(hostUrl:String){
            if (hostUrl.isEmpty()){
                throw IllegalStateException("RequestUrl is empty")
            }
        }


        /**
         * @author gaoxiaoxiong
         * 判断mRequestUrl 是否 /结尾
         */
         fun checkUrlLast(hostUrl:String){
            if (hostUrl.last().toString() != "/") {
                throw IllegalStateException("RequestUrl is 需要以 '/' 结尾，形如www.xxx.com/")
            }
        }

        /**
         * @author gaoxiaoxiong
         * 检查端口
         */
         fun checkUrlPort(hostUrl:String){
            val uri = Uri.parse(hostUrl)
            if (uri.port == Constant.DEFAULT_PORT_80 || uri.port == Constant.DEFAULT_PORT_443) {
                throw IllegalStateException("默认端口号，不用去加上")
            }
        }


        /**
          * 是否为jsonObject
          */
        fun isJsonObject(jsonString: String?): Boolean {
            if (jsonString.isNullOrEmpty()){
                return true
            }
            try {
                JSONObject(jsonString)
                return true // 是有效的 JSONArray
            } catch (e: JSONException) {
                return false // 不是有效的 JSONArray
            }
        }
    }

}