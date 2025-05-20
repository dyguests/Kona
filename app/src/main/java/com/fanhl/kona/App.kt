package com.fanhl.kona

import android.app.Application
import com.fanhl.http.HttpClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        HttpClient.init(this)
    }
}
