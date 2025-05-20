package com.fanhl.kona.kona.repository

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.kona.repository.datasource.KonaDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonaRepository @Inject constructor(
    private val dataSource: KonaDataSource
) {
    suspend fun getCovers(): List<Cover> {
        return dataSource.getCovers()
    }
} 