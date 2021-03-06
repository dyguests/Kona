package com.fanhl.kona.net

import com.fanhl.kona.net.service.PostService
import com.fanhl.kona.net.service.TagService
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

    val postService by lazy { retrofit.create(PostService::class.java)!! }
    val tagService by lazy { retrofit.create(TagService::class.java)!! }

    companion object {
        private const val BASE_URL_EXPLICIT = "https://konachan.com/"
        private const val BASE_URL_SAFE = "https://konachan.net/"


        const val BASE_URL = BASE_URL_EXPLICIT
    }
}