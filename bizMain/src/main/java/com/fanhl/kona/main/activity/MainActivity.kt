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
import com.fanhl.kona.main.screen.PhotoScreen
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
        startDestination = "gallery"
    ) {
        composable("gallery") {
            GalleryScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("photo/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            // TODO: 从 ViewModel 获取图片 URL
            val photoUrl = "https://picsum.photos/800/1200" // 临时使用示例 URL
            PhotoScreen(
                photoId = id,
                photoUrl = photoUrl,
                navController = navController
            )
        }
        // 其他页面路由可以在这里添加
        // composable("detail/{id}") { ... }
        // composable("search") { ... }
    }
} 