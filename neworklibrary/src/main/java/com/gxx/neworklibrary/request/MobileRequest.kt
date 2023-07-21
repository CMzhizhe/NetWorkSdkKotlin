package com.gxx.neworklibrary.request

import android.util.Log
import com.google.gson.Gson
import com.gxx.neworklibrary.BuildConfig
import com.gxx.neworklibrary.inter.OnMobileRequestListener
import com.gxx.neworklibrary.inter.OnOkHttpRequestManagerListener
import java.lang.IllegalStateException

/**
 * @date 创建时间: 2023/7/21
 * @auther gxx
 * @description 请求的封装
 **/
class MobileRequest : OnMobileRequestListener {
    private val TAG = "MobileRequest"
    private val mGson = Gson()

    private var mOnOkHttpRequestManagerListener: OnOkHttpRequestManagerListener? = null

    protected val REQUEST_RESULT_OWN = 1 //自己处理类型

    protected val REQUEST_RESULT_OBJECT = 2 //只返回对象

    protected val REQUEST_RESULT_ARRAY = 3 //只返回数组

    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 拿到 OkHttpRequestManager 实体
     **/
    fun setOnOkHttpRequestManagerListener(listener: OnOkHttpRequestManagerListener) {
        this.mOnOkHttpRequestManagerListener = listener
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/6/24/024
     * @description  get 请求
     * @param method 整个接口名称，包含域名在内
     * @param urlMapString 拼接在url后面
     * @param  isSync true 同步请求
     * @param  requestResultValue 希望服务器给的集合的结果
     */
    override suspend fun doGetRequest(
        method: String,
        urlMapString: Map<String, Any>,
        isSync: Boolean,
        requestResultValue: Int
    ) {
        if (method.isEmpty()) {
            throw IllegalStateException("method 是空的")
        }

        if (mOnOkHttpRequestManagerListener == null) {
            throw IllegalStateException("OnOkHttpRequestManagerListener 没有实现")
        }
    }


    /**
     * @date 创建时间: 2023/7/21
     * @auther gxx
     * @description 异步请求
     **/
    private suspend fun doComposeMapRequest(
        method: String,
        urlMapString: Map<String, Any>,
        requestResultValue: Int
    ) {
        if (mOnOkHttpRequestManagerListener == null) {
            return
        }
       val apiService = mOnOkHttpRequestManagerListener!!.onGetOkHttpRequestManager().createBaseApi()
            .getJson(method, urlMapString)

    }
}