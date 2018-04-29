package com.fanhl.kona

import android.app.Application
import com.fanhl.kona.net.KonaClient
import com.fanhl.util.BlankjUtils
import com.fanhl.kona.db.AppDatabase
import android.arch.persistence.room.Room


class App : Application() {
    val client by lazy { KonaClient() }
    var db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()

    override fun onCreate() {
        super.onCreate()
        BlankjUtils.init(this)
    }
}
