package com.fanhl.kona.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.fanhl.kona.common.activity.BaseActivity
import com.fanhl.kona.main.screen.MainScreen

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
} 