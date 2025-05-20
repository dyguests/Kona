package com.fanhl.kona.kona.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaDataSource @Inject constructor() {
    suspend fun getPost(): List<Cover> {
        return get("https://konachan.com/post.json")
            .await<List<Cover>>()
    }
}