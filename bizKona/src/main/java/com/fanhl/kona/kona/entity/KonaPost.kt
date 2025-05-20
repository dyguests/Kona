package com.fanhl.kona.kona.entity

import com.fanhl.kona.common.entity.Cover
import com.google.gson.annotations.SerializedName

data class KonaPost(
    val id: Int? = null,
    val tags: String? = null,
    @SerializedName("created_at")
    val createdAt: Long? = null,
    @SerializedName("creator_id")
    val creatorId: Int? = null,
    val author: String? = null,
    val change: Int? = null,
    val source: String? = null,
    val score: Int? = null,
    val md5: String? = null,
    @SerializedName("file_size")
    val fileSize: Int? = null,
    @SerializedName("file_url")
    val fileUrl: String? = null,
    @SerializedName("is_shown_in_index")
    val isShownInIndex: Boolean? = null,
    @SerializedName("preview_url")
    val previewUrl: String? = null,
    @SerializedName("preview_width")
    val previewWidth: Int? = null,
    @SerializedName("preview_height")
    val previewHeight: Int? = null,
    @SerializedName("actual_preview_width")
    val actualPreviewWidth: Int? = null,
    @SerializedName("actual_preview_height")
    val actualPreviewHeight: Int? = null,
    @SerializedName("sample_url")
    val sampleUrl: String? = null,
    @SerializedName("sample_width")
    val sampleWidth: Int? = null,
    @SerializedName("sample_height")
    val sampleHeight: Int? = null,
    @SerializedName("sample_file_size")
    val sampleFileSize: Int? = null,
    @SerializedName("jpeg_url")
    val jpegUrl: String? = null,
    @SerializedName("jpeg_width")
    val jpegWidth: Int? = null,
    @SerializedName("jpeg_height")
    val jpegHeight: Int? = null,
    @SerializedName("jpeg_file_size")
    val jpegFileSize: Int? = null,
    val rating: String? = null,
    @SerializedName("has_children")
    val hasChildren: Boolean? = null,
    @SerializedName("parent_id")
    val parentId: Int? = null,
    val status: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    @SerializedName("is_held")
    val isHeld: Boolean? = null,
    @SerializedName("frames_pending_string")
    val framesPendingString: String? = null,
    @SerializedName("frames_pending")
    val framesPending: List<String>? = null,
    @SerializedName("frames_string")
    val framesString: String? = null,
    val frames: List<String>? = null,
)

fun KonaPost.toCover(): Cover {
    return Cover(
        id = id?.toString(),
        title = tags?.split(" ")?.firstOrNull(),
        previewUrl = this@toCover.previewUrl,
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        description = tags,
        author = author,
        date = createdAt?.let { java.util.Date(it * 1000).toString() },
        tags = tags?.split(" ")?.filter { it.isNotBlank() },
        isFavorite = false
    )
}
