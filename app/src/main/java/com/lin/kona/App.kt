package com.lin.kona

import android.app.Application
import com.lin.util.ApplicationContext
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationContext.init(this)
    }
}
