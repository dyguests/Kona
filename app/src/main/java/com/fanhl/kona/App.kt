package com.fanhl.kona

import android.app.Application
import com.fanhl.kona.base.ui.BaseActivity
import com.fanhl.kona.di.AppComponent
import com.fanhl.kona.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.create()
    }
}

val BaseActivity.app: App
    get() {
        return application as App
    }

val BaseActivity.appComponent: AppComponent
    get() {
        return app.appComponent
    }
