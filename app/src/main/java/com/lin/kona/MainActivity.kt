package com.lin.kona

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lin.framework.base.BaseActivity
import com.lin.kona.auth.screen.Welcome
import com.lin.kona.auth.screen.WelcomeScreen
import com.lin.kona.screen.Main
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
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Welcome) {
        composable<Welcome> { WelcomeScreen() }
        composable<Main> { MainScreen() }
    }
}
