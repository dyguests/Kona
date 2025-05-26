package com.fanhl.kona.kona.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.kona.entity.KonaPost
import com.fanhl.kona.kona.entity.toCover
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaDataSource @Inject constructor() {
    suspend fun getPost(
        tags: String,
        page: Int,
    ): List<Cover> {
        // https://konachan.com/post.json?tags=&limit=21&page=3
        return get("/post.json")
            .query("tags" to tags)
            .query("page" to page)
            // .query("limit" to 20)
            .domain(DOMAIN)
            .await<List<KonaPost>>()
            .map { it.toCover() }
    }

    companion object {
        const val DOMAIN = "https://konachan.net/"
    }
}
