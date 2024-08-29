package com.gxx.neworklibrary.model

import com.google.gson.annotations.SerializedName
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.inter.OnCommonParamsListener
import com.gxx.neworklibrary.inter.OnGsonFactoryListener
import com.gxx.neworklibrary.inter.OnOkHttpInterceptorListener

/**
 * @author gaoxiaoxiong
 * 网络配置model，可以自定义
 * @param hostUrl 形如 https://www.wanandroid.com/
 */
class HttpConfigModel(
    @SerializedName("hostUrl")
    var hostUrl:String,
    @SerializedName("connectTime")
    var connectTime:Long = 30 * 1000, //秒
    @SerializedName("readTime")
    var readTime:Long  = 30 * 1000,//秒
    @SerializedName("writeTime")
    var writeTime:Long  = 30 * 1000,//秒
    @SerializedName("retryOnConnection")
    var retryOnConnection:Boolean = true,
){
    companion object{
        const val PARAMS_STATUS_ASC = "ASCENDING";//升序
        const val PARAMS_STATUS_DESC = "DESCENDING";//降序
        const val PARAMS_STATUS_DEFAULT = "DEFAULT";//默认
    }

    var paramsStatus:String = PARAMS_STATUS_DEFAULT//请求参数排序状态
    var errorHandlerFactory: ErrorHandlerFactory? = null//异常错误处理
    var onCommonParamsListener: OnCommonParamsListener? = null//公共参数
    var onGsonFactoryListener: OnGsonFactoryListener? = null //Factory
    var onOkHttpInterceptorListener: OnOkHttpInterceptorListener? = null // 拦截器

}

