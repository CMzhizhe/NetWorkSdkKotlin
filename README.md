# NetWorkSdkKotlin
#### 目录
* demo案例
* 项目介绍
  - maven 配置
  - 依赖引入
  - 域名，intercept，factory配置
  - 自定义BaseBean
  - 统一错误处理
  - 解析data里面的数据，回传业务层成功与失败
  - 提供的请求方法（针对PHP不安规定返回错误的对象处理，比如我要对象，PHP给数组）
* 站在巨人的肩膀上

#### demo案例
```
 /**
      * @date 创建时间: 2023/7/25
      * @auther gxx
      * @description 发起网络请求
      **/
    fun readBanner(){
        viewModelScope.launch{
            WanAndroidMAFRequest.getRequest("banner/json", mutableMapOf(),object :
                DataParseSuFaCall<MutableList<Banner>>() {
                override fun onRequestDataSuccess(data: MutableList<Banner>?) {
                    super.onRequestDataSuccess(data)
                    if(BuildConfig.DEBUG){
                        Log.d(TAG, "json = ${Gson().toJson(data)}");
                    }
                }
            })
        }
    }
```
#### 项目介绍
maven 配置
```
maven { url 'https://jitpack.io' }
```
依赖引入
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
域名，intercept，factory配置
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
这里有其他想说的，底层里面已经配置了GsonFactory、FlowFactory类型的转换。为啥我要说单独处理对于80或者443端口的呢，目的是为了有其他开发者直接修改此库，比如加密、解密的interceptor。域名A的加密/解密跟域名B的不同密匙不同，此时就需要开发者自己去根据域名获取密匙
```
class OkHttpManager {
    companion object{
        private val mCatchMapRetrofit = mutableMapOf<String, Retrofit>()//存储OkHttpManager，key为baseUrl
        private val mObj = Any()
        val mSecreKey = mutableMapOf<String, String>()//key为baseUrl，value为密匙
    }
}
```
```
class EncryptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val request = builder.build()
        val httpUrl =  request.url
        httpUrl.host // www.baidu.com
        httpUrl.port //9999
        httpUrl.scheme//http
        
        return chain.proceed(builder.build())
    }
}
```

