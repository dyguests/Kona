package com.fanhl.kona.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.nfc.Tag

@Dao
interface TagDao {
    @Query("SELECT * FROM Tag")
    fun getAll(): List<Tag>

    @Insert
    fun insertAll(vararg tags: Tag)
}