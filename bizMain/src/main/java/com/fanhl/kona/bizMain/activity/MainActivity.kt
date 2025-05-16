package com.fanhl.kona.bizMain.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.fanhl.kona.bizCommon.activity.BaseActivity
import com.fanhl.kona.bizMain.screen.MainScreen

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
} 