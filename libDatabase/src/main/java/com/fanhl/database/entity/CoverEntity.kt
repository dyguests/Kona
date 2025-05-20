package com.fanhl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "covers")
data class CoverEntity(
    @PrimaryKey
    val id: String,
    val title: String?,
    val previewUrl: String?,
    val previewWidth: Int?,
    val previewHeight: Int?,
    val createdAt: Long = System.currentTimeMillis()
) {
    // fun toCover() = Cover(
    //     id = id,
    //     title = title,
    //     previewUrl = previewUrl,
    //     previewWidth = previewWidth,
    //     previewHeight = previewHeight
    // )

    companion object {
        // fun fromCover(cover: Cover) = CoverEntity(
        //     id = cover.id,
        //     title = cover.title,
        //     previewUrl = cover.previewUrl,
        //     previewWidth = cover.previewWidth,
        //     previewHeight = cover.previewHeight
        // )
    }
} 