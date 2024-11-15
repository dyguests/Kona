package com.lin.kona.gallery.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lin.kona.common.ui.theme.KonaTheme
import com.lin.kona.gallery.viewmodel.GalleryUiIntent
import com.lin.kona.gallery.viewmodel.GalleryViewModel
import kotlinx.serialization.Serializable

@Serializable
object Gallery

@Composable
fun GalleryScreen() {
    val viewModel = hiltViewModel<GalleryViewModel>()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(GalleryUiIntent.LoadGallery)
    }

    KonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            GalleryContent(innerPadding)
        }
    }
}

@Preview
@Composable
private fun GalleryScreenPreview() {
    KonaTheme {
        GalleryScreen()
    }
}


@Composable
private fun GalleryContent(innerPadding: PaddingValues) {
    Text(
        text = "Hello ${"Android"}!",
        modifier = Modifier.padding(innerPadding)
    )
}

@Preview
@Composable
private fun GalleryContentPreview() {
    KonaTheme {
        GalleryContent(PaddingValues())
    }
}
    