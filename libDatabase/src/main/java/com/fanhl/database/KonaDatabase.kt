package com.fanhl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fanhl.database.dao.CoverDao
import com.fanhl.database.entity.CoverEntity
import com.fanhl.database.converter.InstantConverter
import com.fanhl.kona.database.dao.QueryDao
import com.fanhl.kona.database.entity.QueryEntity

@Database(
    entities = [
        CoverEntity::class,
        QueryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class)
abstract class KonaDatabase : RoomDatabase() {
    abstract fun coverDao(): CoverDao
    abstract fun queryDao(): QueryDao
} 