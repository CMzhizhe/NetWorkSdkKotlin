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
	 implementation 'com.github.CMzhizhe:NetWorkSdkKotlin:v1.0.4'
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
        .setRequestUrl(REQUEST_URL_FIRST)
        .setIsDebug(BuildConfig.DEBUG)
        .setOnFactoryListener(FactoryImpl())
        .setOnInterceptorListener(InterceptImpl())
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

