package com.fanhl.kona.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fanhl.kona.database.entity.QueryEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * 搜索查询的数据访问对象。
 *
 * 提供对搜索查询的增删改查操作，包括：
 * - 插入新的查询记录
 * - 更新现有查询的使用次数和时间
 * - 获取最近使用的查询
 * - 获取收藏的查询
 * - 搜索历史查询
 * - 删除查询记录
 */
@Dao
interface QueryDao {
    /**
     * 插入新的查询记录，如果已存在则更新
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: QueryEntity)

    /**
     * 更新查询记录
     */
    @Update
    suspend fun update(query: QueryEntity)

    /**
     * 获取最近使用的查询记录
     * @param limit 返回的最大记录数
     */
    @Query("SELECT * FROM queries ORDER BY lastUsedAt DESC LIMIT :limit")
    fun getRecentQueries(limit: Int = 10): Flow<List<QueryEntity>>

    /**
     * 获取所有收藏的查询记录
     */
    @Query("SELECT * FROM queries WHERE isFavorite = 1 ORDER BY lastUsedAt DESC")
    fun getFavoriteQueries(): Flow<List<QueryEntity>>

    /**
     * 搜索历史查询记录
     * @param searchTerm 搜索关键词
     * @param limit 返回的最大记录数
     */
    @Query("SELECT * FROM queries WHERE query LIKE '%' || :searchTerm || '%' ORDER BY useCount DESC, lastUsedAt DESC LIMIT :limit")
    fun searchQueries(searchTerm: String, limit: Int = 10): Flow<List<QueryEntity>>

    /**
     * 根据查询文本删除记录
     */
    @Query("DELETE FROM queries WHERE query = :query")
    suspend fun deleteByQuery(query: String)

    /**
     * 删除所有查询记录
     */
    @Query("DELETE FROM queries")
    suspend fun deleteAll()

    /**
     * 更新查询记录的使用次数和时间
     * 如果记录不存在则插入新记录
     */
    @Query("""
        INSERT INTO queries (query, lastUsedAt, useCount, isFavorite)
        VALUES (:query, :lastUsedAt, 1, 0)
        ON CONFLICT(query) DO UPDATE SET
            lastUsedAt = :lastUsedAt,
            useCount = useCount + 1
    """)
    suspend fun updateOrInsertQuery(query: String, lastUsedAt: Instant = Instant.now())
} 