package com.fanhl.kona.common.repository

import com.fanhl.kona.common.entity.Cover

/**
 * Base interface for all image repositories.
 * Defines the common contract for retrieving posts from different image sources.
 */
interface IGalleryRepository {
    /**
     * Retrieves a list of posts based on the given tags and page number.
     *
     * @param tags The search tags to filter posts
     * @param page The page number for pagination
     * @return A list of [Cover] objects representing the posts
     */
    suspend fun getPost(tags: String, page: Int): List<Cover>
} 