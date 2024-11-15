package com.lin.util

import android.app.Application
import android.content.Context

object ApplicationContext {
    lateinit var current: Context
        private set

    fun init(app: Application) {
        if (::current.isInitialized) {
            return
        }
        current = app.applicationContext
    }

}