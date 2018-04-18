package com.fanhl.kona

import android.app.Application
import com.fanhl.kona.net.KonaClient

class App : Application() {
    val client by lazy { KonaClient() }
}
