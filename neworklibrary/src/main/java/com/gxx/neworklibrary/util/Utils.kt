package com.gxx.neworklibrary.util

import android.net.Uri
import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.model.HttpConfigModel

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
         fun checkUrlIsEmpty(httpConfigModel: HttpConfigModel){
            if (httpConfigModel.hostUrl.isEmpty()){
                throw IllegalStateException("RequestUrl is empty")
            }
        }


        /**
         * @author gaoxiaoxiong
         * 判断mRequestUrl 是否 /结尾
         */
         fun checkUrlLast(httpConfigModel: HttpConfigModel){
            if (httpConfigModel.hostUrl.last().toString() != "/") {
                throw IllegalStateException("RequestUrl is 需要以 '/' 结尾，形如www.xxx.com/")
            }
        }

        /**
         * @author gaoxiaoxiong
         * 检查端口
         */
         fun checkUrlPort(httpConfigModel: HttpConfigModel){
            val uri = Uri.parse(httpConfigModel.hostUrl)
            if (uri.port == Constant.DEFAULT_PORT_80 || uri.port == Constant.DEFAULT_PORT_443) {
                throw IllegalStateException("默认端口号，不用去加上")
            }
        }
    }

}