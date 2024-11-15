package com.lin.kona.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import com.lin.framework.base.BaseActivity
import com.lin.kona.auth.screen.WelcomeScreen

@Deprecated("废弃")
class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeScreen()
        }
    }
}
