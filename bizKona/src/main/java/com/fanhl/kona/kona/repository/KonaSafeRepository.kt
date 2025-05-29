package com.fanhl.kona.kona.repository

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.repository.IGalleryRepository
import com.fanhl.kona.kona.repository.datasource.KonaDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaSafeRepository @Inject constructor(
    private val dataSource: KonaDataSource
) : IGalleryRepository {
    override suspend fun getPost(tags: String, page: Int): List<Cover> {
        return dataSource.getPost(tags, page, true)
    }
} 