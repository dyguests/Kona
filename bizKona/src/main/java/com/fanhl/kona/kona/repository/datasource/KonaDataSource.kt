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
        safe: Boolean = false,
    ): List<Cover> {
        // https://konachan.com/post.json?tags=&limit=21&page=3
        return get("/post.json")
            .query("tags" to tags)
            .query("page" to page)
            // .query("limit" to 20)
            .domain(getDomain(safe))
            .await<List<KonaPost>>()
            .map { it.toCover() }
    }

    private fun getDomain(safe: Boolean): String = if (!safe) DOMAIN else DOMAIN_SAFE

    companion object {
        const val DOMAIN = "https://konachan.com/"
        const val DOMAIN_SAFE = "https://konachan.net/"
    }
}
