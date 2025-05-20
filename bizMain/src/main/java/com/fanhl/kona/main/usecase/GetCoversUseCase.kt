package com.fanhl.kona.main.usecase

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.kona.repository.KonaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCoversUseCase @Inject constructor(
    private val konaRepository: KonaRepository
) {
    suspend fun execute(): List<Cover> {
        return konaRepository.getPost()
    }
} 