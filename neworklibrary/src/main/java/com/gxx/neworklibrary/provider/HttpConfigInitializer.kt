package com.gxx.neworklibrary.provider

import android.content.Context
import android.net.Uri
import androidx.startup.Initializer
import com.gxx.neworklibrary.OkHttpManager
import com.gxx.neworklibrary.constans.Constant
import com.gxx.neworklibrary.error.exception.AbsApiException
import com.gxx.neworklibrary.error.exception.ExceptionHandle
import com.gxx.neworklibrary.error.factory.ErrorHandlerFactory
import com.gxx.neworklibrary.model.HttpConfigModel
import com.gxx.neworklibrary.util.AssetsHttpConfigRead
import com.gxx.neworklibrary.util.MoshiUtil
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
        val jsonString = AssetsHttpConfigRead.readHttpConfig(HTTP_NAME,context)
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
            throw IllegalStateException("httpConfig类型转换失败")
        }

        checkUrlIsEmpty(httpConfigModel)
        checkUrlIsEmpty(videoConfigModel)

        checkUrlLast(httpConfigModel)
        checkUrlLast(videoConfigModel)

        checkUrlPort(httpConfigModel)
        checkUrlPort(videoConfigModel)

        OkHttpManager.getInstance().init(context)
        OkHttpManager.getInstance().putConfig(HTTP_TYPE_HTTP_BUSINESS,httpConfigModel)
        OkHttpManager.getInstance().putConfig(HTTP_TYPE_VIDEO_BUSINESS,videoConfigModel)

        //设置域名为 wanandroid 的信息配置
        val wanandroidErrorBuilder = ErrorHandlerFactory.Builder()
            .setBaseUrl(httpConfigModel.model.hostUrl)
            .setOnNetWorkErrorListener(this)
            .setOnServiceCodeErrorHandleFinishListener(this)

        //配置 httpBusiness
        OkHttpManager.getInstance().createRetrofit(httpConfigModel,OkHttpManager.Builder().apply {
            mIsShowNetHttpLog = true //打印log日志
            mErrorHandlerFactory = ErrorHandlerFactory(wanandroidErrorBuilder)
        })

        //配置 videoBusiness
        OkHttpManager.getInstance().createRetrofit(videoConfigModel,OkHttpManager.Builder().apply {

        })
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
       return mutableListOf()
    }

    /**
     * @author gaoxiaoxiong
     * 检查端口
     */
    private fun checkUrlPort(httpConfigModel: HttpConfigModel){
        val uri = Uri.parse(httpConfigModel.model.hostUrl)
        if (uri.port == Constant.DEFAULT_PORT_80 || uri.port == Constant.DEFAULT_PORT_443) {
            throw IllegalStateException("默认端口号，不用去加上")
        }
    }

    /**
     * @author gaoxiaoxiong
     * 判断mRequestUrl 是否 /结尾
     */
    private fun checkUrlLast(httpConfigModel: HttpConfigModel){
        if (httpConfigModel.model.hostUrl.last().toString() != "/") {
            throw IllegalStateException("RequestUrl is 需要以 '/' 结尾，形如www.xxx.com/")
        }
    }

    /**
     * @author gaoxiaoxiong
     * 检查是否为空的
     */
    private fun checkUrlIsEmpty(httpConfigModel: HttpConfigModel){
        if (httpConfigModel.model.hostUrl.isEmpty()){
            throw IllegalStateException("RequestUrl is empty")
        }
    }

    /**
     * @author gaoxiaoxiong
     * 网络错误类型的通知
     */
    override fun onNetWorkError(throwable: ExceptionHandle.ResponeThrowable) {

    }

    /**
     * @author gaoxiaoxiong
     * 错误已经处理完毕的回调
     */
    override fun onServiceCodeErrorHandleFinish(error: AbsApiException) {

    }
}