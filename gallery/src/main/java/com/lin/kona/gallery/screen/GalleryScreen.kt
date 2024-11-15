package com.lin.kona.gallery.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.lin.kona.common.ui.theme.KonaTheme
import com.lin.kona.gallery.viewmodel.GalleryUiIntent
import com.lin.kona.gallery.viewmodel.GalleryUiState
import com.lin.kona.gallery.viewmodel.GalleryViewModel
import com.lin.kona.gallery.viewmodel.mockGalleryUiState
import kotlinx.serialization.Serializable

@Serializable
object Gallery

@Composable
fun GalleryScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<GalleryViewModel>()

    val uiState by viewModel.uiStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(GalleryUiIntent.LoadGallery)
    }

    KonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            GalleryContent(uiState, innerPadding, navController)
        }
    }
}

@Composable
private fun GalleryContent(uiState: GalleryUiState, innerPadding: PaddingValues, navController: NavHostController) {
    val galleryList = uiState.galleryList

    // val debouncer = rememberDebouncer()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(galleryList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // debouncer.invoke {
                            //     toast("test")
                            navController.navigate(Photo(it))
                            // }
                        },
                ) {
                    Box {
                        AsyncImage(
                            model = it,
                            contentDescription = "Thumb",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Text(
                            text = "Hello ${"Android"}!",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GalleryContentPreview() {
    KonaTheme {
        GalleryContent(mockGalleryUiState, PaddingValues(), rememberNavController())
    }
}
    