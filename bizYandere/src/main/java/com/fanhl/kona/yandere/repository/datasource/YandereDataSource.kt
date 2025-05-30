package com.fanhl.kona.yandere.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.yandere.entity.YanderePost
import com.fanhl.kona.yandere.entity.toCover
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YandereDataSource @Inject constructor() {
    suspend fun getPost(
        tags: String,
        page: Int,
    ): List<Cover> {
        // https://yande.re/post.json?tags=winter_forest&limit=40&page=2
        return get("/post.json")
            .query("tags" to tags)
            .query("page" to page)
            // .query("limit" to 20)
            .domain(DOMAIN)
            .await<List<YanderePost>>()
            .map { it.toCover() }
    }

    companion object {
        const val DOMAIN = "https://yande.re/"
    }
}
