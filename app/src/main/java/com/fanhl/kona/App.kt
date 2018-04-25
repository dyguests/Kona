package com.fanhl.kona

import android.app.Application
import com.fanhl.kona.net.KonaClient
import com.fanhl.util.BlankjUtils

class App : Application() {
    val client by lazy { KonaClient() }
    override fun onCreate() {
        super.onCreate()
        BlankjUtils.init(this)
    }
}
