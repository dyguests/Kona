package com.fanhl.kona.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fanhl.kona.common.activity.BaseActivity
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.main.navigation.NavRoutes
import com.fanhl.kona.main.screen.GalleryScreen
import com.fanhl.kona.main.screen.PhotoScreen
import com.fanhl.kona.main.viewmodel.PhotoIntent
import com.fanhl.kona.main.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.GALLERY
    ) {
        composable(NavRoutes.GALLERY) {
            GalleryScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable(NavRoutes.PHOTO) { backStackEntry ->
            val cover = backStackEntry.savedStateHandle.get<Cover>(NavRoutes.Args.COVER) ?: return@composable
            PhotoScreen(
                viewModel = hiltViewModel<PhotoViewModel>().apply {
                    handleIntent(PhotoIntent.SetCover(cover))
                },
                navController = navController
            )
        }
        // 其他页面路由可以在这里添加
        // composable("detail/{id}") { ... }
        // composable(NavRoutes.SEARCH) { ... }
    }
} 