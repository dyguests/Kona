package com.fanhl.kona.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.fanhl.kona.db.dao.TagDao
import com.fanhl.kona.model.Tag

@Database(entities = [Tag::class], version = 1)
abstract class KonaDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao

    companion object {
        private const val DATABASE_NAME = "kona"

        fun create(applicationContext: Context) = Room.databaseBuilder(applicationContext, KonaDatabase::class.java, KonaDatabase.DATABASE_NAME).build()
    }
}