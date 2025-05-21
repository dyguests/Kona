package com.fanhl.kona.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 封面
 *
 * 首页列表项数据结构
 */
@Parcelize
data class Cover(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val author: String? = null,
    val date: String? = null,
    val tags: List<String>? = null,
    val isFavorite: Boolean? = false,
    // Preview related
    val previewUrl: String? = null,
    val previewWidth: Int? = null,
    val previewHeight: Int? = null,
    // Sample related
    val sampleUrl: String? = null,
    val sampleWidth: Int? = null,
    val sampleHeight: Int? = null,
    val sampleFileSize: Int? = null,
    // JPEG related
    val jpegUrl: String? = null,
    val jpegWidth: Int? = null,
    val jpegHeight: Int? = null,
    val jpegFileSize: Int? = null,
    // File related
    val fileUrl: String? = null,
    val fileSize: Int? = null,
    val fileExt: String? = null,
) : Parcelable
