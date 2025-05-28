package com.fanhl.kona

import android.app.Application
import com.fanhl.http.HttpClient
import com.fanhl.util.SharePreferenceUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharePreferenceUtil.init(this, "kona_prefs")
        HttpClient.init(this)
    }
}
