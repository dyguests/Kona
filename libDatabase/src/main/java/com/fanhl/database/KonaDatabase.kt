package com.fanhl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fanhl.database.dao.CoverDao
import com.fanhl.database.entity.CoverEntity

@Database(
    entities = [CoverEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KonaDatabase : RoomDatabase() {
    abstract fun coverDao(): CoverDao
} 