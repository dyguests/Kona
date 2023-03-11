package com.lin.kona.net.servcie

import com.lin.kona.model.Post
import retrofit2.http.GET

interface PostService {
    @GET("/quotes")
    suspend fun getQuotes(): List<Post>
}