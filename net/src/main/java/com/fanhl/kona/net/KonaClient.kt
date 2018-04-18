package com.fanhl.kona.net

import android.printservice.PrintService
import com.fanhl.kona.net.service.PostService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class KonaClient {
    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    val postService by lazy { retrofit.create(PostService::class.java) }

    companion object {
//        const val BASE_URL_EXPLICIT = "https://konachan.com/"
        const val BASE_URL_EXPLICIT = "http://konachan.com/"
//        const val BASE_URL_SAFE = "https://konachan.net/"
        const val BASE_URL_SAFE = "http://konachan.net/"
        const val BASE_URL = BASE_URL_SAFE
    }
}