package com.fanhl.kona.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 封面
 *
 * 首页列表项结构结构
 */
@Parcelize
data class Cover(
    val id: String? = null,
    val title: String? = null,
    val previewUrl: String? = null,
    val previewWidth: Int? = null,
    val previewHeight: Int? = null,
    val description: String? = null,
    val author: String? = null,
    val date: String? = null,
    val tags: List<String>? = null,
    val isFavorite: Boolean? = false,
) : Parcelable
