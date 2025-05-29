package com.fanhl.kona.main.usecase

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.danbooru.repository.DanbooruRepository
import com.fanhl.kona.kona.repository.KonaRepository
import com.fanhl.kona.kona.repository.KonaSafeRepository
import com.fanhl.kona.main.entity.SiteType
import com.fanhl.kona.yandere.repository.YandereRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCoversUseCase @Inject constructor(
    private val yandereRepository: YandereRepository,
    private val konaRepository: KonaRepository,
    private val konaSafeRepository: KonaSafeRepository,
    private val danbooruRepository: DanbooruRepository,
) {
    suspend fun execute(siteType: SiteType, query: String, page: Int): List<Cover> {
        return when (siteType) {
            SiteType.Yandere -> yandereRepository.getPost(query, page)
            SiteType.Konachan -> konaRepository.getPost(query, page)
            SiteType.KonachanSafe -> konaSafeRepository.getPost(query, page)
            SiteType.Danbooru -> danbooruRepository.getPost(query, page)
        }
    }
} 