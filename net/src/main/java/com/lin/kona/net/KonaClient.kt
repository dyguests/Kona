package com.lin.kona.net

import com.lin.kona.net.servcie.PostService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KonaClient {
    private const val BASE_URL_NSFW = "https://konachan.com/"
    private const val BASE_URL = "https://konachan.net/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // .addCallAdapterFactory()
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Suppress("HasPlatformType")
    val postService by lazy { retrofit.create(PostService::class.java) }
}