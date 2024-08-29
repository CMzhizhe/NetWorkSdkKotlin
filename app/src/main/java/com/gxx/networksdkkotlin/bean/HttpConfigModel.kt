package com.gxx.networksdkkotlin.bean

class HttpConfigModel  (
    val httpBusiness: Business,
    val videoBusiness: Business
)

data class Business (
    val hostURL: String,
    val connectTime: Long,
    val readTime: Long,
    val retryOnConnection: Boolean,
    val writeTime: Long
)
