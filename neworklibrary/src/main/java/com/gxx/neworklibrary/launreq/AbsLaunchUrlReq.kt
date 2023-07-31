package com.gxx.neworklibrary.launreq

import com.gxx.neworklibrary.okbuild.ParamOkBuilder

/**
  * @date 创建时间: 2023/7/28
  * @auther gxx
  * @description  请求，域名
  **/
abstract class AbsLaunchUrlReq {

    /**
      * @date 创建时间: 2023/7/28
      * @auther gxx
      * @description
     *  构建 PamOkBuilder 可为null
      **/
    abstract fun createParamOkBuilder(): ParamOkBuilder?


    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/7/29/029
     * @description  baseUrl
     **/
    abstract fun baseUrl():String
}