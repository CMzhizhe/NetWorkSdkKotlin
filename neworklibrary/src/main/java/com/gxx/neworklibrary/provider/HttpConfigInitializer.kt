package com.gxx.neworklibrary.provider

import android.content.Context
import androidx.startup.Initializer
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.NetWorkExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.model.HttpConfigModel
import com.gxx.neworklibrary.util.AssetsHttpConfigRead
import com.gxx.neworklibrary.util.MoshiUtil
import com.gxx.neworklibrary.util.Utils
import org.json.JSONObject

class HttpConfigInitializer: Initializer<Unit>, ErrorHandlerFactory.OnNetWorkErrorListener,
    ErrorHandlerFactory.OnServiceCodeErrorHandleFinishListener {
    companion object{
        const val HTTP_NAME = "http_config.json"
        //业务类型的接口配置
        const val HTTP_TYPE_HTTP_BUSINESS="httpBusiness"
        const val HTTP_TYPE_VIDEO_BUSINESS="videoBusiness"
    }

    override fun create(context: Context) {
        //读取配置信息
        val jsonString = AssetsHttpConfigRead.readHttpConfig(context,HTTP_NAME)
        if (jsonString.isEmpty()){
            throw IllegalStateException("jsonString 是空的")
        }

        var httpJSON:JSONObject? = null
        try {
            httpJSON = JSONObject(jsonString).getJSONObject(HTTP_TYPE_HTTP_BUSINESS)
        }catch (e:Exception){
            e.printStackTrace()
        }

        if (httpJSON == null){
            throw IllegalStateException("httpJSON 是空的")
        }

        var videoJSON :JSONObject? = null
        try {
            videoJSON  =  JSONObject(jsonString).getJSONObject(HTTP_TYPE_VIDEO_BUSINESS)
        }catch (e:Exception){
            e.printStackTrace()
        }

        if (videoJSON == null){
            throw IllegalStateException("videoJSON 是空的")
        }

        val httpConfigModel = MoshiUtil.fromJson<HttpConfigModel>(httpJSON.toString())
        val videoConfigModel = MoshiUtil.fromJson<HttpConfigModel>(videoJSON.toString())

        if (httpConfigModel == null || videoConfigModel == null){
            throw IllegalStateException("类型转换失败")
        }

        Utils.checkUrlIsEmpty(httpConfigModel)
        Utils.checkUrlIsEmpty(videoConfigModel)

        Utils.checkUrlLast(httpConfigModel)
        Utils.checkUrlLast(videoConfigModel)

        Utils.checkUrlPort(httpConfigModel)
        Utils.checkUrlPort(videoConfigModel)

        OkHttpManager.getInstance().init(context)
        OkHttpManager.getInstance().putConfig(HTTP_TYPE_HTTP_BUSINESS,httpConfigModel)
        OkHttpManager.getInstance().putConfig(HTTP_TYPE_VIDEO_BUSINESS,videoConfigModel)


    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }



    /**
     * @author gaoxiaoxiong
     * 网络错误类型的通知
     */
    override fun onNetWorkError(throwable: NetWorkExceptionHandle.ResponeThrowable) {

    }

    /**
     * @author gaoxiaoxiong
     * 错误已经处理完毕的回调
     */
    override fun onServiceCodeErrorHandleFinish(error: AbsApiException) {

    }
}