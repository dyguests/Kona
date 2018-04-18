package com.fanhl.kona.net

import retrofit2.Retrofit

class KonaClient {
//    private retrofit by lazy{Retrofit.Builder().build()}

    companion object {
        const val BASE_URL_EXPLICIT = "https://konachan.com/"
        const val BASE_URL_SAFE = "https://konachan.net/"
        const val BASE_URL = BASE_URL_SAFE
    }
}