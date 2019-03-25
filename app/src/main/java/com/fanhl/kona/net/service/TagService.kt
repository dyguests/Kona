package com.fanhl.kona.net.service

import com.fanhl.kona.net.model.Tag
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TagService {
    @GET("/tag.json")
    fun getTag(
        @Query("tags") tags: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): Deferred<List<Tag>>
}
