package com.gxx.neworklibrary.inter

import com.gxx.neworklibrary.OkHttpRequestManager

/**
  * @date 创建时间: 2023/7/21
  * @auther gxx
  * @description 拿到 OkHttpRequestManager
  **/
interface OnOkHttpRequestManagerListener {
    fun onGetOkHttpRequestManager():OkHttpRequestManager
}