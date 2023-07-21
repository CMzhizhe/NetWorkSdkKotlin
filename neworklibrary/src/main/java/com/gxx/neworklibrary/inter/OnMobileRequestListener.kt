package com.gxx.neworklibrary.inter

interface OnMobileRequestListener {

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description  get 请求
     * @param method 整个接口名称，包含域名在内
     * @param urlMapString 拼接在url后面
     * @param  isSync true 同步请求
     * @param  requestResultValue 希望服务器给的集合的结果
     */
   suspend fun doGetRequest(
        method: String,
        urlMapString: Map<String, Any>,
        isSync: Boolean,
        requestResultValue: Int
    )

}