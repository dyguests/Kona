package com.fanhl.kona.kona.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.kona.entity.KonaPost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaDataSource @Inject constructor() {
    suspend fun getPost(): List<Cover> {
        // return get("https://konachan.com/post.json")
        return get("/post.json")
            .domain(DOMAIN)
            .await<List<KonaPost>>()
            .map { it.toCover() }
    }

    companion object {
        const val DOMAIN = "https://konachan.net/"
    }
}

private fun KonaPost.toCover(): Cover {
    return Cover(
        id = id?.toString(),
        title = tags?.split(" ")?.firstOrNull(),
        imageUrl = previewUrl,
        description = tags,
        author = author,
        date = createdAt?.let { java.util.Date(it * 1000).toString() },
        tags = tags?.split(" ")?.filter { it.isNotBlank() },
        isFavorite = false
    )
}
