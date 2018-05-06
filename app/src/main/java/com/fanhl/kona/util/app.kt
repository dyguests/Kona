package com.fanhl.kona.util

import android.view.View
import com.fanhl.kona.App

val View.app: App
    get() {
        return context.applicationContext as App
    }