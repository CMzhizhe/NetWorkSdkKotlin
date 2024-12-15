package com.gxx.neworklibrary.request

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.apiservice.BaseApiService
import com.gxx.neworklibrary.constans.EmRequestType
import com.gxx.neworklibrary.error.impl.NoNetWorkApiException
import com.gxx.neworklibrary.error.impl.ParamsApiException
import com.gxx.neworklibrary.inter.OnJavaRequestFailListener
import com.gxx.neworklibrary.inter.OnJavaRequestSuccessListener
import com.gxx.neworklibrary.inter.OnResponseBodyTransformJsonListener
import com.gxx.neworklibrary.model.RqParamModel
import com.gxx.neworklibrary.request.parsestring.JsonParseResult
import com.gxx.neworklibrary.util.NetWorkUtil
import com.gxx.neworklibrary.util.Utils
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call

open class JavaSyncRequest(private val mOnResponseBodyTransformJsonListener: OnResponseBodyTransformJsonListener) {
    private val mJsonParseResult:JsonParseResult = JsonParseResult()


    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description get 请求同步
     */
    fun getSync(rqParamModel: RqParamModel, onSuccessListener: OnJavaRequestSuccessListener?,
                onFailListener: OnJavaRequestFailListener?){
        doSyncRequest(rqParamModel,EmRequestType.GET,onSuccessListener,onFailListener)
    }

    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description post请求同步
     */
    fun postSync(rqParamModel: RqParamModel, onSuccessListener: OnJavaRequestSuccessListener?,
                 onFailListener: OnJavaRequestFailListener?){
        doSyncRequest(rqParamModel,EmRequestType.POST,onSuccessListener,onFailListener)
    }


    /**
     * @date 创建时间: 2024/12/15
     * @author gaoxiaoxiong
     * @description 同步请求
     */
    private fun doSyncRequest(rqParamModel: RqParamModel,
                              emRequestType: EmRequestType,
                              onSuccessListener: OnJavaRequestSuccessListener?,
                              onFailListener: OnJavaRequestFailListener?
                              ){
        if (rqParamModel.hostUrl.isEmpty()) {
            throw ParamsApiException(errorMessage = "hostUrl 是空的")
        }

        if (rqParamModel.funName.isEmpty()) {
            throw ParamsApiException(errorMessage = "funName 是空的")
        }

        if (OkHttpManager.getRetrofitAndConfigModel(hostUrl = rqParamModel.hostUrl) == null) {
            throw ParamsApiException(errorMessage = "请调用setHttpConfig")
        }


        if (!NetWorkUtil.isNetConnected()) {
            onFailListener?.onJavaRequestFail(method = "${rqParamModel.hostUrl}${rqParamModel.funName}", exception = NoNetWorkApiException())
            return
        }

        //检查传递的类型是否为 jsonObject
        if (!rqParamModel.jsonBodyModel.isNullOrEmpty() && !Utils.isJsonObject(rqParamModel.jsonBodyModel)){
            throw ParamsApiException(errorMessage = "传递的类型，不是jsonObject，你需要将model，转成jsonObject，请调用 GsonUtils.toJson()")
        }


        var  linkedHashMap:java.util.LinkedHashMap<String,Any> ? = null;
        val retrofitAndConfigModel = OkHttpManager.getRetrofitAndConfigModel(hostUrl = rqParamModel.hostUrl)!!
        if (emRequestType!=EmRequestType.GET && !rqParamModel.jsonBodyModel.isNullOrEmpty()){
            val jsonObject = JSONObject(rqParamModel.jsonBodyModel!!)
            if (retrofitAndConfigModel.httpConfigModel.onCommonParamsListener!=null){
                for ((key,model) in retrofitAndConfigModel.httpConfigModel.onCommonParamsListener!!.onCommonParams()) {
                    jsonObject.putOpt(key,model)
                }
            }

            //最终将公共参数与业务参数进行集合转成一个map
            linkedHashMap = GsonUtils.fromJson(jsonObject.toString(),object : TypeToken<LinkedHashMap<String, Any>>() {}.type)
        }


        val method = "${rqParamModel.hostUrl}${rqParamModel.funName}"
        val aipService = OkHttpManager.getRetrofit(rqParamModel.hostUrl).create(BaseApiService::class.java)
        var responseBody: ResponseBody? = null
        var call:Call<ResponseBody>? = null
        val urlMap = rqParamModel.urlMap ?: LinkedHashMap()
        runCatching {
            call = if (emRequestType == EmRequestType.GET){
                 aipService.javaGetJson(method, urlMap ?: mutableMapOf())
            }else{
                if (!linkedHashMap.isNullOrEmpty()) {
                      aipService.javaPostJson(
                        method,
                        urlMap,
                        linkedHashMap
                    )
                } else {
                     aipService.javaPostJson(method, urlMap ?: mutableMapOf())
                }
            }
            val response = call?.execute()
            if (response!=null && response.isSuccessful){
                responseBody = response.body()
                val content = responseBody?.string()
                if (!content.isNullOrEmpty()){
                  val listener = mOnResponseBodyTransformJsonListener.onResponseBodyTransformJson(method, content)
                    mJsonParseResult.javaDoIParseResult(method,rqParamModel.emResultType,listener,onSuccessListener,onFailListener)
                } else {
                    onFailListener?.onJavaRequestFail(method)
                }
            }else{
                onFailListener?.onJavaRequestFail(method)
            }
        }.onFailure {
            it.printStackTrace()
            onFailListener?.onJavaRequestFail(method,it)
        }
    }
}