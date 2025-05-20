package com.fanhl.kona.yandere.repository

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.yandere.repository.datasource.YandereDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YandereRepository @Inject constructor(
    private val dataSource: YandereDataSource
) {
    suspend fun getPost(): List<Cover> {
        return dataSource.getPost()
    }
} 