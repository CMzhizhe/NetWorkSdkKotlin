package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.apiservice.BaseApiService

/**
  * @date 创建时间: 2023/7/21
  * @auther gxx
  * @description 拿到 BaseApiService
  **/
interface OnBaseApiServiceListener {
    /**
      * @date 创建时间: 2023/7/27
      * @auther gxx
      * @description
      **/
    fun onGetBaseApiService(): BaseApiService
}