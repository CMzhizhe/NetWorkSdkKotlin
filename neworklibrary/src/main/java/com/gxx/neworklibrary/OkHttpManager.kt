package com.gxx.neworklibrary

import android.app.Application
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.gxx.neworklibrary.interceptor.JsonUtf8Interceptor
import com.gxx.neworklibrary.model.HttpConfigModel
import com.gxx.neworklibrary.model.RetrofitAndConfigModel
import com.gxx.neworklibrary.util.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @date 创建时间: 2023/7/27
 * @auther gxx
 * @description 管理所有的 Retrofit
 **/
object OkHttpManager {
    private val TAG = "OkHttpManager"

    private val mCreateRetrofitMap = mutableMapOf<String, RetrofitAndConfigModel>()//存储配置信息，key为hostUrl
    private lateinit var mApplication: Application

    class Builder {
         var application:Application? = null
         var isShowHttpLog = false//是否打印网络日志
         val catchHttpConfigMap = mutableMapOf<String, HttpConfigModel>()//存储配置信息，key为hostUrl

        fun setApplication(application: Application):Builder {
            this.application = application
            return this
        }

        fun setShowHttpLog(isShowNetHttpLog:Boolean):Builder {
            this.isShowHttpLog = isShowNetHttpLog;
            return this
        }

        fun setHttpConfig(httpConfigModel: HttpConfigModel):Builder {
            catchHttpConfigMap[httpConfigModel.hostUrl] = httpConfigModel
            return this
        }


        fun build(){
            if (application == null){
                throw IllegalStateException("未设置application")
            }

            if (catchHttpConfigMap.isEmpty()){
                throw IllegalStateException("请调用setHttpConfig")
            }

            for (httpConfigModel in catchHttpConfigMap){

                 if (httpConfigModel.value.hostUrl.isEmpty()){
                     throw IllegalStateException("未设置 hostUrl ")
                 }

                Utils.checkUrlIsEmpty(httpConfigModel.value.hostUrl)
                Utils.checkUrlIsEmpty(httpConfigModel.value.hostUrl)

                Utils.checkUrlLast(httpConfigModel.value.hostUrl)
                Utils.checkUrlLast(httpConfigModel.value.hostUrl)

                Utils.checkUrlPort(httpConfigModel.value.hostUrl)
                Utils.checkUrlPort(httpConfigModel.value.hostUrl)

            }

           init(builder = this)
        }

    }

    private fun init(builder: Builder){
        this.mApplication = builder.application!!;
        for (httpConfigModel in builder.catchHttpConfigMap){
            val retrofit = createRetrofit(httpConfigModel = httpConfigModel.value, isShowNetHttpLog = builder.isShowHttpLog)
            val model = RetrofitAndConfigModel(retrofit = retrofit,httpConfigModel = httpConfigModel.value)
            mCreateRetrofitMap[httpConfigModel.value.hostUrl] = model
        }
    }


    /**
      * 构建 Retrofit
      */
   private fun createRetrofit(httpConfigModel: HttpConfigModel,isShowNetHttpLog:Boolean): Retrofit {
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(httpConfigModel.connectTime, TimeUnit.MINUTES)
            .readTimeout(httpConfigModel.readTime, TimeUnit.MINUTES)
            .writeTimeout(httpConfigModel.writeTime, TimeUnit.MINUTES)
            .retryOnConnectionFailure(httpConfigModel.retryOnConnection)

        okBuilder.apply {
            //添加 application/json; charset=UTF-8
            addInterceptor(JsonUtf8Interceptor())
            //打印日志
            if (isShowNetHttpLog) {
                addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            //添加拦截器
            httpConfigModel.onOkHttpInterceptorListener?.let {
                for (interceptor in it.normalInterceptors()) {
                    addInterceptor(interceptor)
                }

                for (netWorkInterceptor in it.netWorkInterceptors()) {
                    addNetworkInterceptor(netWorkInterceptor)
                }
            }
        }


        val reBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(httpConfigModel.hostUrl)
            .client(okBuilder.build())

       reBuilder.apply {
           val gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).create()
           GsonUtils.setGsonDelegate(gson)

           if (httpConfigModel.onGsonFactoryListener!=null){
               if (httpConfigModel.onGsonFactoryListener!!.converterFactoryList().isEmpty()){
                   addConverterFactory(GsonConverterFactory.create(gson))
               }else{
                   for (converterFactory in httpConfigModel.onGsonFactoryListener!!.converterFactoryList()) {
                       addConverterFactory(converterFactory)
                   }
               }

               for (converterFactory in httpConfigModel.onGsonFactoryListener!!.callAdapterFactoryList()) {
                   addCallAdapterFactory(converterFactory)
               }

           }else{
               addConverterFactory(GsonConverterFactory.create(gson))
           }
       }

        return reBuilder.build()
    }

    fun getApplication():Application{
        return mApplication
    }

    fun getRetrofitAndConfigModel(hostUrl: String):RetrofitAndConfigModel?{
        return mCreateRetrofitMap[hostUrl]
    }


    fun getCreateRetrofitMap(): MutableMap<String, RetrofitAndConfigModel> {
        return mCreateRetrofitMap
    }


    fun getRetrofit(hostUrl: String): Retrofit {
        return mCreateRetrofitMap[hostUrl]!!.retrofit
    }


    fun <T> getApi(hostUrl: String, clazz: Class<T>): T {
        return getRetrofit(hostUrl).create(clazz)
    }


}