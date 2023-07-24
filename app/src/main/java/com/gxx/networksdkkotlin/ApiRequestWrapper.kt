package com.gxx.networksdkkotlin


/*
class ApiRequestWrapper(private val requestHelper: RequestHelper) {
    suspend fun <T> process(call: suspend () -> Response<T>): Flow<RequestResult<Response<T>>> {
        return requestHelper.request(call).map { result ->
            when (result) {
                is RequestResult.Success -> result
                is RequestResult.Error -> {
                    // 处理需要登录的情况，例如跳转到登录页面或显示提示信息
                    when (result.error) {
                        is NetworkException -> {
                            // 处理网络异常
                        }
                        is TimeoutException -> {
                            // 处理超时异常
                        }
                        is LoginException -> {
                            // 处理登陆相关异常
                        }
                        is ApiException -> {
                            // 处理通用异常
                        }
                        else -> {
                            // 处理Android异常
                        }
                    }
                    result
                }
            }
        }
    }
}*/
