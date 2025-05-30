package com.fanhl.http

import android.app.Application
import com.fanhl.http.interceptor.HttpLoggingInterceptor
import com.fanhl.util.GsonUtils
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.converter.GsonConverter
import java.util.concurrent.TimeUnit

object HttpClient {
    // /**
    //  * 默认域名
    //  */
    // @DefaultDomain // 设置为默认域名
    // const val baseUrl: String = "https://konachan.net/"

    fun init(application: Application) {
        // 配置OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor()) // 使用自定义的日志拦截器
            .build()

        // 配置RxHttp
        RxHttpPlugins.init(okHttpClient)
            .setDebug(true) // 开启调试模式
            .setConverter(GsonConverter.create(GsonUtils.gson))// 设置数据转换器
            // .setOnParamAssembly { param ->
            //     // 在这里可以统一添加请求头等参数
            //     param.addHeader("Content-Type", "application/json")
            //     // 可以添加token等认证信息
            //     // param.addHeader("Authorization", "Bearer ${getToken()}")
            // }
    }
} 