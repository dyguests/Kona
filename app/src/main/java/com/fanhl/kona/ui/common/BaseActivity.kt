package com.fanhl.kona.ui.common

import android.support.v7.app.AppCompatActivity
import com.fanhl.kona.App

abstract class BaseActivity : AppCompatActivity() {
    val app: App
        get() {
            return application as App
        }

}
