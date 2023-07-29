package com.gxx.neworklibrary.launreq

import com.gxx.neworklibrary.okbuild.ParamOkBuilder
import retrofit2.Retrofit

/**
  * @date 创建时间: 2023/7/28
  * @auther gxx
  * @description  请求，域名
  **/
abstract class AbsLaunchUrlReq {

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 构建 PamOkBuilder
      **/
    abstract fun createParamOkBuilder(): ParamOkBuilder?


    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description 构建retrofit2
      **/
    abstract fun createRetrofit2():Retrofit?

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/7/29/029
     * @description  baseUrl
     **/
    abstract fun baseUrl():String
}