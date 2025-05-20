package com.fanhl.kona.yandere.repository.datasource

import com.fanhl.http.get
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.yandere.entity.YanderePost
import com.fanhl.kona.yandere.entity.toCover
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
