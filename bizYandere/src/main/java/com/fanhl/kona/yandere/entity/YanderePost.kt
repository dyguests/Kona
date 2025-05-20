package com.fanhl.kona.yandere.entity

import com.fanhl.kona.common.entity.Cover

data class YanderePost(
    val id: Int? = null,
    val tags: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val creatorId: Int? = null,
    val approverId: Int? = null,
    val author: String? = null,
    val change: Int? = null,
    val source: String? = null,
    val score: Int? = null,
    val md5: String? = null,
    val fileSize: Int? = null,
    val fileExt: String? = null,
    val fileUrl: String? = null,
    val isShownInIndex: Boolean? = null,
    val previewUrl: String? = null,
    val previewWidth: Int? = null,
    val previewHeight: Int? = null,
    val actualPreviewWidth: Int? = null,
    val actualPreviewHeight: Int? = null,
    val sampleUrl: String? = null,
    val sampleWidth: Int? = null,
    val sampleHeight: Int? = null,
    val sampleFileSize: Int? = null,
    val jpegUrl: String? = null,
    val jpegWidth: Int? = null,
    val jpegHeight: Int? = null,
    val jpegFileSize: Int? = null,
    val rating: String? = null,
    val isRatingLocked: Boolean? = null,
    val hasChildren: Boolean? = null,
    val parentId: Int? = null,
    val status: String? = null,
    val isPending: Boolean? = null,
    val width: Int? = null,
    val height: Int? = null,
    val isHeld: Boolean? = null,
    val framesPendingString: String? = null,
    val framesPending: List<String>? = null,
    val framesString: String? = null,
    val frames: List<String>? = null,
    val isNoteLocked: Boolean? = null,
    val lastNotedAt: Long? = null,
    val lastCommentedAt: Long? = null
)

fun YanderePost.toCover(): Cover {
    return Cover(
        id = id?.toString(),
        title = tags?.split(" ")?.firstOrNull(),
        previewUrl = this@toCover.previewUrl,
        description = tags,
        author = author,
        date = createdAt?.let { java.util.Date(it * 1000).toString() },
        tags = tags?.split(" ")?.filter { it.isNotBlank() },
        isFavorite = false
    )
}