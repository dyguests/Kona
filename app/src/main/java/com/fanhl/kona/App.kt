package com.fanhl.kona

import android.app.Application

class App : Application() {
    val client by lazy{ KonaClient() }
}
