package com.fanhl.kona

import android.app.Application
import com.fanhl.kona.db.KonaDatabase
import com.fanhl.kona.net.KonaClient
import com.fanhl.util.BlankjUtils


class App : Application() {
    val client by lazy { KonaClient() }
    val db by lazy { KonaDatabase.create(this) }

    override fun onCreate() {
        super.onCreate()
        BlankjUtils.init(this)
    }
}
