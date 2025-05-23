package com.fanhl.kona.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * 表示应用中的搜索查询实体。
 *
 * @property query 搜索查询文本，作为主键
 * @property lastUsedAt 最后一次使用此查询的时间
 * @property useCount 此查询被使用的次数
 * @property isFavorite 此查询是否被标记为收藏
 */
@Entity(tableName = "queries")
data class QueryEntity(
    @PrimaryKey val query: String,
    val lastUsedAt: Instant = Instant.now(),
    val useCount: Int = 1,
    val isFavorite: Boolean = false
) 