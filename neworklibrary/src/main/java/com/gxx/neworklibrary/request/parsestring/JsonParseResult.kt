package com.gxx.neworklibrary.request.parsestring

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.gxx.neworklibrary.constans.EmResultType
import com.gxx.neworklibrary.inter.OnIParserListener
import com.gxx.neworklibrary.inter.OnRequestFailListener
import com.gxx.neworklibrary.inter.OnRequestSuccessListener

/**
 * @date 创建时间: 2023/7/23
 * @auther gaoxiaoxiong
 * @description json 结果处理
 **/
class JsonParseResult() {
    /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description 处理结果
     * @param method 接口名称
     * @param emResultType 希望返回的类型
     * @param onRequestFailListener 失败回调
     * @param onRequestSuccessListener 成功回调
     **/
  suspend fun doIParseResult(
        method: String,
        emResultType: EmResultType,
        listener: OnIParserListener,
        onRequestSuccessListener: OnRequestSuccessListener?,
        onRequestFailListener: OnRequestFailListener?
    ) {
        if (emResultType == EmResultType.REQUEST_RESULT_OWN) {//自己处理
            if (listener.isSuccess()){
                onRequestSuccessListener?.onRequestSuccess(method, listener.resultDataJsonElement(), listener)
            }else{
                onRequestFailListener?.onRequestFail(method = method, onIParserListener = listener)
            }
        } else {
            if (listener.isSuccess()) {
                if (listener.resultDataJsonElement() == null) {
                    onRequestSuccessListener?.onRequestSuccess(method, null, listener)
                } else {
                    if (emResultType == EmResultType.REQUEST_RESULT_OBJECT) {//希望拿到对象
                        if (listener.resultDataJsonElement() is JsonObject) {
                            onRequestSuccessListener?.onRequestSuccess(method, listener.resultDataJsonElement(), listener)
                        } else {
                            onRequestFailListener?.onRequestFail(method,  onIParserListener =listener)
                        }
                    } else if (emResultType == EmResultType.REQUEST_RESULT_ARRAY) {//希望拿到数组
                        if (listener.resultDataJsonElement() is JsonArray) {
                            onRequestSuccessListener?.onRequestSuccess(method, listener.resultDataJsonElement(), listener)
                        } else {
                            onRequestFailListener?.onRequestFail(method =method, onIParserListener = listener
                            )
                        }
                    }
                }
            } else {//失败处理
                onRequestFailListener?.onRequestFail(method =method , onIParserListener =listener)
            }
        }
    }
}