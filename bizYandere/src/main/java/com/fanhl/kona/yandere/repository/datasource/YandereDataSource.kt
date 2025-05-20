package com.fanhl.kona.yandere.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.yandere.entity.YanderePost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YandereDataSource @Inject constructor() {
    suspend fun getPost(): List<Cover> {
        // https://yande.re/post.json?tags=winter_forest&limit=40&page=2
        return get("/post.json")
            .domain(DOMAIN)
            .await<List<YanderePost>>()
            .map { it.toCover() }
    }

    companion object {
        const val DOMAIN = "https://yande.re/"
    }
}

private fun YanderePost.toCover(): Cover {
    return Cover(
        id = id?.toString(),
        title = tags?.split(" ")?.firstOrNull(),
        previewUrl = this@toCover.previewUrl,
        description = tags,
        author = author,
        date = createdAt?.let { java.util.Date(it * 1000).toString() },
        tags = tags?.split(" ")?.filter { it.isNotBlank() },
        isFavorite = false
    )
} 