package com.lin.kona.net.servcie

import com.lin.kona.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {
    @GET("/post.json")
    suspend fun getPosts(
        @Query("tags") tags: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Response<List<Post>>
}