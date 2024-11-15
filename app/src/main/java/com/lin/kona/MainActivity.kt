package com.lin.kona

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lin.framework.base.BaseActivity
import com.lin.kona.auth.screen.Welcome
import com.lin.kona.auth.screen.WelcomeScreen
import com.lin.kona.gallery.screen.Gallery
import com.lin.kona.gallery.screen.GalleryScreen
import com.lin.kona.gallery.screen.Photo
import com.lin.kona.gallery.screen.PhotoScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        composable<Welcome> {
            WelcomeScreen(
                galleryNavi = {
                    navController.navigate(Gallery)
                }
            )
        }
        composable<Gallery> { GalleryScreen(navController) }
        composable<Photo> { PhotoScreen(it.toRoute<Photo>().url) }
    }
}
