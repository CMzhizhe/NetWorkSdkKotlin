package com.gxx.neworklibrary.launreq

import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.request.MobileRequest

/**
  * @date 创建时间: 2023/7/28
  * @auther gxx
  * @description  请求，域名，错误工厂
  **/
abstract class AbsLaunchUrlReq {
    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 请求的域名
      **/
    abstract fun getBaseUrl():String

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 网络请求request
      **/
    abstract fun getMobileRequest(): MobileRequest


    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 拿到错误工厂处理
      **/
    abstract fun getErrorHandlerFactory():ErrorHandlerFactory
}