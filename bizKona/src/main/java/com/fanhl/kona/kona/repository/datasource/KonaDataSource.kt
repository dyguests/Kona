package com.fanhl.kona.kona.repository.datasource

import com.fanhl.kona.common.entity.Cover
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaDataSource @Inject constructor() {
    suspend fun getCovers(): List<Cover> {
        // Mock data for testing
        return listOf(
            Cover(
                id = "1",
                title = "Beautiful Sunset",
                imageUrl = "https://img04.sogoucdn.com/v2/thumb/crop/xy/ai/w/60/h/60?appid=122&url=https://i04piccdn.sogoucdn.com/ea9826c634eb8093",
                description = "A stunning sunset over the ocean",
                author = "John Doe",
                date = "2024-03-20",
                tags = listOf("sunset", "ocean", "nature"),
                isFavorite = false
            ),
            Cover(
                id = "2",
                title = "Mountain View",
                imageUrl = "https://img04.sogoucdn.com/v2/thumb/crop/xy/ai/w/60/h/60?appid=122&url=https://i04piccdn.sogoucdn.com/ea9826c634eb8093",
                description = "Majestic mountains at dawn",
                author = "Jane Smith",
                date = "2024-03-19",
                tags = listOf("mountain", "dawn", "landscape"),
                isFavorite = true
            ),
            Cover(
                id = "3",
                title = "City Lights",
                imageUrl = "https://img04.sogoucdn.com/v2/thumb/crop/xy/ai/w/60/h/60?appid=122&url=https://i04piccdn.sogoucdn.com/ea9826c634eb8093",
                description = "Urban nightscape with city lights",
                author = "Mike Johnson",
                date = "2024-03-18",
                tags = listOf("city", "night", "urban"),
                isFavorite = false
            )
        )
    }
}