package com.fanhl.kona.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.fanhl.kona.model.Tag
import io.reactivex.Flowable

@Dao
interface TagDao {
    @Query("SELECT * FROM Tag")
    fun getAll(): Flowable<List<Tag>>

    @Insert
    fun insertAll(vararg tags: Tag)
}