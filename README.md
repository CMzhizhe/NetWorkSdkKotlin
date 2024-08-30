# NetWorkSdkKotlin
#### 目录
* demo案例
* 项目介绍
  - 依赖引入
  - 配置拦截器，自定义错误，公共参数
  - 解析服务器提供的json
  - 自定义BaseBean
  - 解析data里面的数据，统一错误处理，回传业务层成功与失败
  - 提供的请求方法（针对PHP不安规定返回错误的对象处理，比如我要对象，PHP给数组）
  - flow方式的拿到结果
  - 自定义Api
* 站在巨人的肩膀上

以下介绍，只能讲个大概，建议跑Demo，我更多的希望各位开发者，可以自定义修改此库来。因为加密，解密，以及配置Content-Type各自需求不同。喜欢再点个start

#### demo案例
```
viewModelScope.launch {
            WanAndroidMAFRequest.postRequest(
                "banner/json",
                BannerRequestModel(
                    "123", mutableListOf("11","18")
                ),null,object :
                    ServiceDataParseCall<MutableList<Banner>>() {
                    override suspend fun onRequestBaseBeanSuccess(
                        data: MutableList<Banner>?,
                        baseBean: BaseBean
                    ) {
                        super.onRequestBaseBeanSuccess(data, baseBean)
                        if (data!=null){
                            _bannerShareFlow.emit(data!!)
                        }
                    }
                }
            )
        }
```
#### 项目介绍
##### 依赖引入
```
dependencies {
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'

    //okhttp
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    // Retrofit 库
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    implementation 'com.google.code.gson:gson:2.10.1'

    // Kotlin
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.0'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2'
    implementation "androidx.activity:activity-ktx:1.6.0"
    implementation "androidx.fragment:fragment-ktx:1.3.6"

    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.4.1'
    implementation "androidx.lifecycle:lifecycle-runtime:2.4.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1"


    implementation 'com.blankj:utilcodex:1.31.1'
}
```
##### 配置拦截器，自定义错误，公共参数
```
private val mInterceptor = mutableListOf<Interceptor>(SortInterceptor())
OkHttpManager.Builder()
            .setApplication(application)
            .setShowHttpLog(true)
            .setHttpConfig(HttpConfigModel(mHostUrl).apply {
                //拦截器
                onOkHttpInterceptorListener = object : OnOkHttpInterceptorListener {
                    override fun normalInterceptors(): List<Interceptor> {
                        //todo 可以在这里加解密等处理
                        return mInterceptor
                    }

                    override fun netWorkInterceptors(): List<Interceptor> {
                        return mutableListOf()
                    }

                }
                //公共参数
                onCommonParamsListener =  object : OnCommonParamsListener{
                    override fun onCommonParams(): LinkedHashMap<String, Any> {
                        return LinkedHashMap<String, Any>().apply {
                            put("age","12")
                            put("sexList", mutableListOf("female","male"))
                        }
                    }
                }
                //自定义错误
                errorHandlerFactory = ErrorHandlerFactory.Builder()
                    .addErrorHandler(LoginErrorHandler(),LoginApiException(ERROR_CODE_100.toString()))
                    .addErrorHandler(PayErrorHandler(),PayApiException(ERROR_CODE_101.toString()))
                    .addErrorHandler(TokenErrorHandler(),TokenApiException(ERROR_CODE_102.toString()))
                    .setOnNetWorkErrorListener(this@WanAndroidMAFRequest)//网络错误的回调
                    .setOnServiceCodeErrorHandleFinishListener(this@WanAndroidMAFRequest)//所有的错误，可以交由一个地方处理
                    .build()
            })
            .build()

 
```
 
 
##### 解析服务器提供的json
需要自己new 一个类，去实现 OnResponseBodyTransformJsonListener
```
open class ServiceDataTransform : OnResponseBodyTransformJsonListener {
    companion object {
        const val ERROR_CODE = "errorCode"
        const val DATA = "data"
        const val ERROR_CODE_TYPE_0 = "0"//与服务器协商的正常状态
    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/12/012
     * @description
     * @param jsString 服务器提供的根json
     * @param method 域名+端口+接口名称
     **/
    override fun onResponseBodyTransformJson(
        method: String,
        jsString: String
    ): OnIParserListener {
        val baseBean: BaseBean

        if (jsString.isEmpty()){
            throw RootJsonEmptyApiException()
        }

        val jsElement = JsonParser.parseString(jsString)

        if (jsElement.isJsonObject) {//服务器提供的是jsonObject
            val jsonObject = jsElement.asJsonObject
            val errorCode = jsonObject.get(ERROR_CODE).asInt

            baseBean = if (errorCode.toString() == ERROR_CODE_TYPE_0) {
                if (jsonObject.get(DATA).isJsonArray) {
                    BaseBean(method, jsString, jsonObject.getAsJsonArray(DATA), errorCode.toString(),"")
                } else {
                    BaseBean(method, jsString, jsonObject.getAsJsonObject(DATA), errorCode.toString(),"")
                }
            } else {
                //与服务器协商的异常逻辑
                // 可以在这里抛异常，比如服务器有提供code = 201，那么可以抛出属于201的异常错误
               val baseUrl = Utils.getBaseUrlByMethod(method)
               val errorHandlerFactory = OkHttpManager.getRetrofitAndConfigModel(baseUrl)?.httpConfigModel?.errorHandlerFactory
                if (errorHandlerFactory!=null){
                    for (serviceErrorApiException in errorHandlerFactory.getServiceErrorApiExceptions()) {
                        if (serviceErrorApiException.code == errorCode.toString()){
                            serviceErrorApiException.jsString = jsString
                            serviceErrorApiException.errorMessage = "这里可以写错误的信息"
                            throw serviceErrorApiException
                        }
                    }
                }
               //未设置任何异常错误信息
                throw UnApiException(errorCode.toString(), jsString)
            }
        } else {
            //理论上这是永远不会触发的
            baseBean = BaseBean(method, jsString, jsElement, "-1","")
        }
        return baseBean
    }
}
```
在这里，我有定义，我跟服务器之间的异常处理，这里进行异常抛出，在最终的onRequestFail拿到错误，丢给对应的Handler进行处理

##### 自定义BaseBean
自己创建一个class文件去实现OnIParserListener接口
```
class BaseBean(var method: String? = null,
               var resourceJsonString: String? = null,
               var jsonElement: JsonElement? = null,
               var errorCode:String,msg:String="") : OnIParserListener {
    override fun resultDataJsonElement(): JsonElement? {
        return jsonElement
    }

    override fun sourceJsonString(): String? {
        return resourceJsonString;
    }

    override fun isSuccess(): Boolean {
        return errorCode == ERROR_CODE_TYPE_0.toString()
    }
}
```
##### 解析data里面的数据，统一错误处理，回传业务层成功与失败
```
open class ServiceDataParseCall<T> : OnRequestSuccessListener, OnRequestFailListener {
    private val TAG = "DataParseSuFaCall"

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 成功结果回调
     **/
    override suspend fun onRequestSuccess(
        method: String,
        targetElement: JsonElement?,
        onIParserListener: OnIParserListener
    ) {
        if (targetElement!=null){
            var result:Any?=null
            try {
                val parameterizedType = this::class.java.genericSuperclass as ParameterizedType
                val subType =  parameterizedType.actualTypeArguments.first() //获取泛型T
                result = GsonUtils.fromJson(targetElement.toString(),subType)
            } catch (e: Exception) {
                e.printStackTrace()
                //处理解析异常
                onRequestFail(method,e,"","解析异常", null,onIParserListener)
                return
            }

            onRequestBaseBeanSuccess(if (result == null) null else result as T,
                onIParserListener as BaseBean
            )
        }else{
            onRequestBaseBeanSuccess(null, onIParserListener as BaseBean)
        }
    }

    /**
     * @date 创建时间: 2023/7/23
     * @auther gaoxiaoxiong
     * @description 失败接口的调用
     **/
    override suspend fun onRequestFail(
        method: String,
        throwable: Throwable?,
        status: String?,
        failMsg: String?,
        errorJsonString: String?,
        onIParserListener: OnIParserListener?
    ) {
        if (throwable!=null){
            val baseUrl = Utils.getBaseUrlByMethod(method)
            val cacheHandler = OkHttpManager.getRetrofitAndConfigModel(baseUrl)?.httpConfigModel?.errorHandlerFactory
            if (cacheHandler!=null){
                if (throwable is AbsApiException){//处理服务器的异常
                    cacheHandler.rollServiceCodeGateError(cacheHandler.getServiceErrorHandlers().first(),throwable)
                }else{//处理网络异常
                    cacheHandler.netWorkException(throwable)
                }
            }
        }
        onRequestDataFail(status?:"", failMsg?:"", onIParserListener as BaseBean?)

    }

    /**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  请求失败
     **/
    open suspend fun onRequestDataFail(code: String, msg: String, baseBean: BaseBean?=null) {}



   /**
    * @author gaoxiaoxiong
    * @date 创建时间: 2023/8/6/006
    * @description  请求成功
    * 返回含有 BaseBean 的
    **/
    open suspend fun onRequestBaseBeanSuccess(data: T?, baseBean: BaseBean) {}
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

 //自定义错误factory的构建
  val mErrorHandlerFactory = ErrorHandlerFactory()
        .addErrorHandler(LoginErrorHandler())
        .init()
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
##### flow方式的拿到结果
flow方式，其实原理也是通过接口方式拿到结果，然后通过callbackFlow拿到数据
```
/**
     * @author gaoxiaoxiong
     * @date 创建时间: 2023/8/6/006
     * @description  flow方式的调用
     **/
    suspend inline fun <reified T> createRequestFlow(funName: String) = callbackFlow<T> {
        val dataParseSuFaCall = object : DataParseSuFaCall<T>() {
            override fun onRequestDataSuccess(data: T?) {
                super.onRequestDataSuccess(data)
                trySend(data!!)
            }
        }
        mMobileRequest.get(
            RqParamModel(
                baseUrl = REQUEST_URL_FIRST,
                funName = funName,
                null,
                urlMap = mutableMapOf()
            ), dataParseSuFaCall, dataParseSuFaCall
        )
        awaitClose { }
    }

/**
     * @date 创建时间: 2023/7/25
     * @auther gxx
     * @description 发起网络请求
     **/
    fun readBanner(){
        viewModelScope.launch{
            WanAndroidMAFRequest.createRequestFlow<MutableList<Banner>>("banner/json").collect{//flow方式
                mBannerFlow.emit(Gson().toJson(it))
            }
        }
    }
```

#### 自定义Api
```
    interface CustomApiService {
        //自定义接口名称
        @JvmSuppressWildcards
        @GET
        suspend fun readBook(
            @Url url: String,
            @QueryMap urlMap: Map<String, Any>
        ): ResponseBody
    }

   private val mJsonParseResult = JsonParseResult()
   //自定义api请求的Demo
    suspend fun <T> readBannerJson(dataParseSuFaCall: DataParseSuFaCall<T>) {
        val api = mOkHttpManager.getApi(REQUEST_URL_FIRST, CustomApiService::class.java)
        val url = "${REQUEST_URL_FIRST}banner/json"
        val responseBody = api?.readBook(url, mutableMapOf())
        mMobileRequest.responseBodyTransformJson(REQUEST_URL_FIRST,"banner/json",responseBody,dataParseSuFaCall).collect{
            if (it == null) {
                return@collect
            }
            mJsonParseResult.doIParseResult(
                "${REQUEST_URL_FIRST}banner/json",
                EmResultType.REQUEST_RESULT_OWN,
                listener = it,
                dataParseSuFaCall,
                dataParseSuFaCall
            )
        }
    }
   
```


##### 站在巨人的肩膀上
[开发搭建网络请求框架 3](https://juejin.cn/post/7220339259161526333)这里借用了他的链式处理的思想，点赞点赞

