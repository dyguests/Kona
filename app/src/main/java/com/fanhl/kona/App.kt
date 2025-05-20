package com.fanhl.kona

import android.app.Application
import com.fanhl.http.HttpClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        HttpClient.init(this)
    }
}
