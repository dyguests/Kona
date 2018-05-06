package com.fanhl.kona.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.fanhl.kona.model.Post
import io.reactivex.Flowable

@Dao
interface PostDao {
    @Query("SELECT * FROM Post ORDER BY updateTime")
    fun getAll(): Flowable<List<Post>>

    @Query("SELECT * FROM Post ORDER BY updateTime DESC LIMIT 1")
    fun getLast(): Flowable<Post>

    @Query("SELECT * FROM Post ORDER BY updateTime DESC LIMIT :limit")
    fun getLast(limit: Int? = 1): Flowable<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tags: Post)

    companion object {

    }
}