package com.lin.kona.gallery.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lin.kona.common.ui.theme.KonaTheme
import com.lin.kona.gallery.viewmodel.GalleryViewModel
import kotlinx.serialization.Serializable

@Serializable
object Gallery

@Composable
fun GalleryScreen() {
    val viewModel = hiltViewModel<GalleryViewModel>()

    LaunchedEffect(Unit) {
        viewModel
    }

    KonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = "Hello ${"Android"}!",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}