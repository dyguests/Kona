package com.fanhl.kona.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.fanhl.kona.model.Tag
import io.reactivex.Flowable


@Dao
interface TagDao {
    @Query("SELECT * FROM Tag ORDER BY updateTime")
    fun getAll(): Flowable<List<Tag>>

    @Query("SELECT * FROM Tag WHERE name LIKE :name")
    fun getAllLikeName(name: String): Flowable<List<Tag>>

    @Query("SELECT * FROM Tag ORDER BY updateTime DESC LIMIT 1")
    fun getLast(): Flowable<Tag>

    @Query("SELECT * FROM Tag ORDER BY updateTime DESC LIMIT 1")
    fun getLastSync(): Tag?

    @Query("SELECT * FROM Tag ORDER BY updateTime DESC LIMIT :limit")
    fun getLast(limit: Int? = 1): Flowable<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tags: Tag)

    @Query("SELECT count(*) FROM TAG")
    fun getCount(): Flowable<Long>

//    fun insertAllAsync(vararg tags: Tag): Flowable<Unit> {
//        return Flowable.fromCallable { insertAll(*tags) }
//    }
}