package com.lin.kona

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.lin.framework.base.BaseActivity
import com.lin.kona.screen.MainScreen

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

}

@Composable
private fun MyApp() {
    MainScreen()
}
