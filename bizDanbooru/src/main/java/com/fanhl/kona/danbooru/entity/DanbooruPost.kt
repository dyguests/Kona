package com.fanhl.kona.danbooru.entity

import com.fanhl.kona.common.entity.Cover
import com.google.gson.annotations.SerializedName

data class DanbooruPost(
    val id: Int? = null,
    val tags: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
    @SerializedName("updated_at") val updatedAt: Long? = null,
    @SerializedName("creator_id") val creatorId: Int? = null,
    val author: String? = null,
    val source: String? = null,
    val score: Int? = null,
    val md5: String? = null,
    @SerializedName("file_size") val fileSize: Int? = null,
    @SerializedName("file_ext") val fileExt: String? = null,
    @SerializedName("file_url") val fileUrl: String? = null,
    @SerializedName("preview_url") val previewUrl: String? = null,
    @SerializedName("preview_width") val previewWidth: Int? = null,
    @SerializedName("preview_height") val previewHeight: Int? = null,
    @SerializedName("sample_url") val sampleUrl: String? = null,
    @SerializedName("sample_width") val sampleWidth: Int? = null,
    @SerializedName("sample_height") val sampleHeight: Int? = null,
    @SerializedName("sample_file_size") val sampleFileSize: Int? = null,
    @SerializedName("jpeg_url") val jpegUrl: String? = null,
    @SerializedName("jpeg_width") val jpegWidth: Int? = null,
    @SerializedName("jpeg_height") val jpegHeight: Int? = null,
    @SerializedName("jpeg_file_size") val jpegFileSize: Int? = null,
    val rating: String? = null,
    val width: Int? = null,
    val height: Int? = null
)

fun DanbooruPost.toCover(): Cover {
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
        isFavorite = false,
        // File related
        fileUrl = fileUrl,
        fileSize = fileSize,
        fileExt = fileExt,
        // Sample related
        sampleUrl = sampleUrl,
        sampleWidth = sampleWidth,
        sampleHeight = sampleHeight,
        sampleFileSize = sampleFileSize,
        // JPEG related
        jpegUrl = jpegUrl,
        jpegWidth = jpegWidth,
        jpegHeight = jpegHeight,
        jpegFileSize = jpegFileSize
    )
} 