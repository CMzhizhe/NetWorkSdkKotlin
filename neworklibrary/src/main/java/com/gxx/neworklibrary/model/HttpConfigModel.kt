package com.gxx.neworklibrary.model

/**
 * @author gaoxiaoxiong
 * 网络配置model，可以自定义
 */
class HttpConfigModel(val name:String,val model:Config)

class Config{
    var connectTime:Int = 30
    var readTime:Int  = 30
    var writeTime:Int  = 30
    var retryOnConnection:Boolean = true
    var hostUrl:String = ""
}