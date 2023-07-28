package com.gxx.neworklibrary.launreq

import com.gxx.neworklibrary.okbuild.ReqOkBuilder
import retrofit2.Retrofit

/**
  * @date 创建时间: 2023/7/28
  * @auther gxx
  * @description  请求，域名，错误工厂
  **/
abstract class AbsLaunchUrlReq {


    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 构建 PamOkBuilder
      **/
    abstract fun createReqOkBuilder(): ReqOkBuilder?


    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 构建retrofit2
      **/
    abstract fun createRetrofit2():Retrofit?



}