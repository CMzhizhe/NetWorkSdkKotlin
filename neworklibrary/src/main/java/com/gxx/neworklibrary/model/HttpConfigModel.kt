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
    var connectTime:Int = 15, //秒
    @SerializedName("readTime")
    var readTime:Int  = 15,//秒
    @SerializedName("writeTime")
    var writeTime:Int  = 15,//秒
    @SerializedName("retryOnConnection")
    var retryOnConnection:Boolean = true,
){
    var errorHandlerFactory: ErrorHandlerFactory? = null//异常错误处理
    var onCommonParamsListener: OnCommonParamsListener? = null//公共参数
    var onGsonFactoryListener: OnGsonFactoryListener? = null //Factory
    var onOkHttpInterceptorListener: OnOkHttpInterceptorListener? = null // 拦截器

}

