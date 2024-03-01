package com.gxx.neworklibrary.model


import android.os.Parcelable
import com.gxx.neworklibrary.constans.EmResultType

/**
 * @date 创建时间: 2023/7/23
 * @auther gaoxiaoxiong
 * @description 请求参数封装类型
 * @param hostUrl 基础的Url
 * @param funName 请求的接口名称
 * @param bodyModel put 和 post 需要的参数，model 类必须 : Parcelable
 * @param urlMap 拼接在url后面的参数
 * @param emResultType 对想要的服务器结果提供的类型  自己处理类型   只返回对象  只返回数组
 **/
class RqParamModel<T : Parcelable>(
    var hostUrl:String = "",
    var funName: String = "",
    var bodyModel: T? = null,
    var urlMap: Map<String, Any>? = null,
    var emResultType: EmResultType = EmResultType.REQUEST_RESULT_OWN
)