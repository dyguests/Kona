package com.fanhl.kona.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.fanhl.kona.model.Tag

@Database(entities = [Tag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}