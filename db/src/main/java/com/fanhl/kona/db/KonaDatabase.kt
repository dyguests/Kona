package com.fanhl.kona.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.fanhl.kona.db.dao.PostDao
import com.fanhl.kona.db.dao.TagDao
import com.fanhl.kona.model.Post
import com.fanhl.kona.model.Tag

@Database(entities = [Post::class, Tag::class], version = 6)
abstract class KonaDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun postDao(): PostDao

    companion object {
        private const val DATABASE_NAME = "kona"

        fun create(applicationContext: Context) = Room.databaseBuilder(applicationContext, KonaDatabase::class.java, KonaDatabase.DATABASE_NAME)
                .addMigrations(object : Migration(4, 5) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE Tag ADD COLUMN updateTime INTEGER")
                    }
                })
                .build()
    }
}