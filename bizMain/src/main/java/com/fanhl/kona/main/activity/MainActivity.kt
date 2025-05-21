package com.fanhl.kona.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fanhl.kona.common.activity.BaseActivity
import com.fanhl.kona.main.screen.GalleryScreen
import com.fanhl.kona.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(viewModel = viewModel)
        }
    }
}

@Composable
private fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            GalleryScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        // 其他页面路由可以在这里添加
        // composable("detail/{id}") { ... }
        // composable("search") { ... }
    }
} 