package com.fanhl.kona.net.service

import com.fanhl.kona.net.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {
    @GET("/post.json")
    fun getPost(
        @Query("tags") tags: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Deferred<List<Post>>
}
