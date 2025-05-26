package com.fanhl.kona.danbooru.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.danbooru.entity.DanbooruPost
import com.fanhl.kona.danbooru.entity.toCover
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanbooruDataSource @Inject constructor() {
    suspend fun getPost(
        tags: String,
        page: Int,
    ): List<Cover> {
        // https://danbooru.donmai.us/posts.json?tags=winter_forest&page=2
        return get("/posts.json")
            .query("tags" to tags)
            .query("page" to page)
            // .query("limit" to 20)
            .domain(DOMAIN)
            .await<List<DanbooruPost>>()
            .map { it.toCover() }
    }

    companion object {
        const val DOMAIN = "https://danbooru.donmai.us/"
    }
} 