package com.fanhl.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fanhl.database.entity.CoverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoverDao {
    @Query("SELECT * FROM covers ORDER BY createdAt DESC")
    fun getAllCovers(): Flow<List<CoverEntity>>

    @Query("SELECT * FROM covers WHERE title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchCovers(query: String): Flow<List<CoverEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCovers(covers: List<CoverEntity>)

    @Query("DELETE FROM covers")
    suspend fun deleteAllCovers()
} 