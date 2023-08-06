# NetWorkSdkKotlin
#### 目录
* demo案例
* 项目介绍
  - maven 配置
  - 依赖引入
  - 域名，intercept，factory配置
  - 解析服务器提供的json
  - 自定义BaseBean
  - 解析data里面的数据，统一错误处理，回传业务层成功与失败
  - 提供的请求方法（针对PHP不安规定返回错误的对象处理，比如我要对象，PHP给数组）
* 站在巨人的肩膀上

以下介绍，只能讲个大概，建议跑Demo，我更多的希望各位开发者，可以自定义修改此库来。因为加密，解密，以及配置Content-Type各自需求不同。

#### demo案例
```
 /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner(){
        viewModelScope.launch{
           val map = mutableMapOf<String,Any>()
            map["userId"] = "1"
            WanAndroidMAFRequest.getRequest("banner/json",map,object :
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG){
                        Log.d(TAG, "json = ${Gson().toJson(data)}");
                        Log.d(TAG, "是否主线程 = ${Looper.getMainLooper() == Looper.myLooper()}");
                    }
                }
            })
        }
    }
```
#### 项目介绍
##### maven 配置
```
maven { url 'https://jitpack.io' }
```
##### 依赖引入
```
dependencies {
   // Kotlin
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.0'

    //okhttp
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    // Retrofit 库build.gradle.kts
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.google.code.gson:gson:2.9.0'

    // Kotlin 协程库
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2'

    implementation 'com.github.CMzhizhe:Retrofit-FlowCallAdapterFactory:v1.0.0'
}
```
##### 域名，intercept，factory配置
```
object WanAndroidMAFRequest : OnBaseApiServiceListener {
    val TAG = "WanAndroidMAFRequest"
    //配置域名
    val REQUEST_URL_FIRST = "https://www.wanandroid.com/"
    val mMobileRequest: MobileRequest = MobileRequest(this, ServiceDataTransform())
    val mOkHttpManager: OkHttpManager = OkHttpManager.Builder()
        .setRequestUrl(REQUEST_URL_FIRST) //必选，如果默认端口是80 或者 443 就不用额外添加
        .setIsDebug(BuildConfig.DEBUG) //可选
        .setOnFactoryListener(FactoryImpl()) //可选，默认已配置GsonFactory  FlowFactory
        .setOnInterceptorListener(InterceptImpl())  //可选
        .build()

    //自定义错误factory的构建
    val mErrorHandlerFactory = ErrorHandlerFactory()
        .addErrorHandler(LoginErrorHandler())
        .addErrorHandler(PayErrorHandler())
        .addErrorHandler(TokenErrorHandler())
        .addErrorHandler(UnErrorHandler())
        .init()

    override fun onGetBaseApiService(): BaseApiService? {
        return mOkHttpManager.getApi(REQUEST_URL_FIRST,BaseApiService::class.java)
    }
}
```
这里有其他想说的，底层里面已经配置了GsonFactory、FlowFactory类型的转换。为啥我要说单独处理对于80或者443端口的呢，目的是为了有其他开发者直接修改此库，比如加密、解密的interceptor。域名A的加密/解密跟域名B的不同密匙不同，此时就需要开发者自己去根据域名获取密匙。以下是伪代码
```
class OkHttpManager {
    companion object{
        private val mCatchMapRetrofit = mutableMapOf<String, Retrofit>()//存储OkHttpManager，key为baseUrl
        private val mObj = Any()
        val mSecreKey = mutableMapOf<String, String>()//key为baseUrl，value为密匙 key类似=http://www.baidu.com/
    }
}
```
```
//加密
class EncryptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = builder.build()
        val httpUrl = request.url
        httpUrl.host //www.baidu.com
        httpUrl.port//9999
        httpUrl.scheme//http
        //http://www.baidu.com:9999/
        val port = HttpUrl.defaultPort(httpUrl.scheme)
        var url = ""
        if (port == 80 || port == 443) {
            url = "${httpUrl.scheme}://${httpUrl.host}/"
        } else {
            url = "${httpUrl.scheme}://${httpUrl.host}:${httpUrl.port}/"
        }
        val secreKey = OkHttpManager.mSecreKey[url]
        //TODO 加密
        return chain.proceed(builder.build())
    }
}
```
##### 解析服务器提供的json
需要自己new 一个类，去实现 OnResponseBodyTransformJsonListener
```
class ServiceDataTransform : OnResponseBodyTransformJsonListener {
    companion object {
        const val ERROR_CODE = "errorCode"
        const val DATA = "data"
        const val ERROR_CODE_TYPE_0 = "0"//与服务器协商的正常状态
        const val ERROR_CODE_TYPE_101 = "101"//与服务器协商错误的逻辑
        const val ERROR_CODE_TYPE_102 = "102"//与服务器协商错误的逻辑
        const val ERROR_CODE_TYPE_103 = "103"//与服务器协商错误的逻辑
    }

    override fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean: BaseBean
        if (JsonParser.parseString(jsString).isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = JsonParser.parseString(jsString).asJsonObject
            val errorCode = jsonObject.get(ERROR_CODE).asInt
            baseBean = if (errorCode.toString() == ERROR_CODE_TYPE_0) {
                if (jsonObject.get(DATA).isJsonArray) {
                    BaseBean(method, jsString, jsonObject.getAsJsonArray(DATA), errorCode)
                } else {
                    BaseBean(method, jsString, jsonObject.getAsJsonObject(DATA), errorCode)
                }
            } else {//与服务器协商的异常逻辑
                // 可以在这里抛异常
                if (errorCode.toString() == ERROR_CODE_TYPE_101) {
                    throw LoginApiException(errorCode.toString(), jsString, "登陆的异常")
                } else if (errorCode.toString() == ERROR_CODE_TYPE_102) {
                    throw PayApiException(errorCode.toString(), jsString, "支付的异常")
                } else if (errorCode.toString() == ERROR_CODE_TYPE_103) {
                    throw TokenApiException(errorCode.toString(), jsString, "token的异常")
                } else {
                    throw UnApiException(errorCode.toString(), jsString, "未跟服务器定义的异常")
                }
            }
        } else {
            baseBean = BaseBean(method, jsString, null, -1)
        }
        return baseBean
    }
}
```
在这里，我有定义，我跟服务器之间的异常处理，这里进行异常抛出，在最终的onRequestFail拿到错误，丢给对应的Handler进行处理

##### 自定义BaseBean
自己创建一个class文件去实现OnIParserListener接口
```
{
  errorCode:0
  data:{}
  msg:"ok"
}
class BaseBean( var resourceJsonString: String? = null,
               var jsonElement: JsonElement? = null,
               var errorCode:Int) : OnIParserListener {
    //这里是获取data里面的 JsonElement
    override fun resultDataJsonElement(): JsonElement? {
        return jsonElement
    }
    //这个是整个服务器提供的json格式
    override fun sourceJsonString(): String? {
        return resourceJsonString;
    }
    //这里是决定，是否服务器返回的正常数据
    override fun isSuccess(): Boolean {
        return errorCode == 0
    }
}
```
##### 解析data里面的数据，统一错误处理，回传业务层成功与失败
我们在发起网络请求的时候是这样的，new DataParseSuFaCall 传递需要的具体格式
```
viewModelScope.launch{
            val map = mutableMapOf<String,Any>()
            map["userId"] = "1"
            WanAndroidMAFRequest.getRequest("banner/json",map,object :
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {//成功回调
                    super.onRequestDataSuccess(data)
                }
                override fun onRequestBaseBeanFail(baseBean: BaseBean?) {//失败回调
                    super.onRequestBaseBeanFail(baseBean)
                }
            })
        }

/**
 * @date 创建时间: 2023/7/22
 * @auther gaoxiaoxiong
 * @description 服务器数据处理
 **/
open class DataParseSuFaCall<T> : AbsRequestResultImpl() {
    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 成功结果回调
     **/
    override fun onRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    ) {
        if (targetElement!=null){
            var result:Any?=null
            try {
                val parameterizedType = this::class.java.genericSuperclass as ParameterizedType
                val subType =  parameterizedType.actualTypeArguments.first() //获取泛型T
                val adapter: JsonAdapter<Any> = MoshiUtil.moshi.adapter(subType)
                result = adapter.fromJson(targetElement.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                //处理解析异常
                onRequestFail(e,"","解析异常", null,onIParserListener)
            }
            onRequestDataSuccess(if (result == null) null else result as T)
            onRequestBaseBeanSuccess(if (result == null) null else result as T,
                onIParserListener as BaseBean
            )
        }else{
            onRequestDataSuccess(null)
            onRequestBaseBeanSuccess(null, onIParserListener as BaseBean)
        }
    }

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 失败接口的调用
     **/
    override fun onRequestFail(
        throwable: Throwable?,
        status: String?,
        failMsg: String?,
        errorJsonString: String?,
        onIParserListener: OnIParserListener?
    ) {
        if (throwable!=null){
            val resPoneThrowable = WanAndroidMAFRequest.mErrorHandlerFactory.netWorkException(throwable)
            //自定义解析错误处理，这里是处理，你跟服务器之间定义好的错误信息
            if (resPoneThrowable.code == ExceptionHandle.ERROR.UNKNOWN.toString() && throwable is AbsApiException){
WanAndroidMAFRequest.mErrorHandlerFactory.rollGateError(WanAndroidMAFRequest.mErrorHandlerFactory.getErrorHandlers().first(),throwable)
            }
        }
        onRequestDataFail(status?:"", failMsg?:"", onIParserListener as BaseBean?)
        onRequestBaseBeanFail(onIParserListener as BaseBean? )
    }

      /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求失败
     **/
    open fun onRequestDataFail(code: String, msg: String, baseBean: BaseBean?=null) {}

      /**
       * @author gaoxiaoxiong
       * @date 创建时间: 2023/8/6/006
       * @description  请求失败
       **/
    open fun onRequestBaseBeanFail(baseBean: BaseBean?=null) {}

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求成功
     **/
    open fun onRequestDataSuccess(data: T?) {}

   /**
    * @author gaoxiaoxiong
    * @date 创建时间: 2023/8/6/006
    * @description  请求成功
    * 返回含有 BaseBean 的
    **/
    open fun onRequestBaseBeanSuccess(data: T?, baseBean: BaseBean) {}
}
```
这里再定义错误收集处理方法，比如我们登录错误了
```
/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 登陆错误异常
 **/
class LoginApiException(code: String="", jsString: String="", errorMessage: String = "") :
    AbsApiException(code, jsString, errorMessage) {
}
```
既然错误了，那么就的要有对应的Handler处理这个错误
```
/**
 * @date 创建时间: 2023/7/24
 * @auther gaoxiaoxiong
 * @description 错误handler处理
 **/
class LoginErrorHandler(override var next: OnErrorHandler? = null) : OnErrorHandler {
    private val TAG = "LoginErrorHandler"
    override  fun handleError(error: AbsApiException): Boolean {
        if (error is LoginApiException){
            if(BuildConfig.DEBUG){
              Log.d(TAG, "${LoginErrorHandler::class.simpleName}已处理异常");
            }
            return true
        }else{
            return false
        }
    }
}
```
##### 提供的请求方法（针对PHP不安规定返回错误的对象处理，比如我要对象，PHP给数组）
```
  /**
     * @date 创建时间: 2023/7/22
     * @auther gaoxiaoxiong
     * @description get 请求
     **/
    suspend fun <T> getRequest(
        funName: String,
        urlMap: Map<String, Any> = mutableMapOf(),
        dataParseSuFaCall: DataParseSuFaCall<T>
    ) {
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = urlMap,
                emResultType = EmResultType.REQUEST_RESULT_OBJECT  //这里是定义你想要的类型的，默认是obj
            ), dataParseSuFaCall, dataParseSuFaCall
        )
    }
```
##### 站在巨人的肩膀上
[开发搭建网络请求框架 3](https://juejin.cn/post/7220339259161526333)这里借用了他的链式处理的思想，点赞点赞

