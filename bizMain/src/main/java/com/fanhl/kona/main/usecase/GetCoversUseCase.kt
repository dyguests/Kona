package com.fanhl.kona.main.usecase

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.kona.repository.KonaRepository
import com.fanhl.kona.yandere.repository.YandereRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCoversUseCase @Inject constructor(
    private val konaRepository: KonaRepository,
    private val yandereRepository: YandereRepository,
) {
    suspend fun execute(tags: String): List<Cover> {
        return yandereRepository.getPost(tags)
    }
} 