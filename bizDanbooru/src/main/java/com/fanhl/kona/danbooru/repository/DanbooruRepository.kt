package com.fanhl.kona.danbooru.repository

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.repository.IGalleryRepository
import com.fanhl.kona.danbooru.repository.datasource.DanbooruDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanbooruRepository @Inject constructor(
    private val dataSource: DanbooruDataSource
) : IGalleryRepository {
    override suspend fun getPost(tags: String, page: Int): List<Cover> {
        return dataSource.getPost(tags, page)
    }
} 