package com.fanhl.kona.common.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.content.res.Configuration

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set window flags for full transparency
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        
        // Set status bar color to transparent and adjust icon color based on theme
        WindowCompat.getInsetsController(window, window.decorView).apply {
            // 检查当前系统主题是否为深色模式
            val isDarkMode = resources.configuration.uiMode and 
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            isAppearanceLightStatusBars = !isDarkMode // 深色主题用浅色图标，浅色主题用深色图标
        }
    }
} 