package com.fanhl.kona.main.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.ui.theme.KonaTheme
import com.fanhl.kona.main.viewmodel.PhotoEffect
import com.fanhl.kona.main.viewmodel.PhotoIntent
import com.fanhl.kona.main.viewmodel.PhotoState
import com.fanhl.kona.main.viewmodel.PhotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    viewModel: PhotoViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PhotoEffect.DownloadStarted -> {
                    Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
                }
                is PhotoEffect.FileExists -> {
                    Toast.makeText(context, "File already exists", Toast.LENGTH_SHORT).show()
                }
                is PhotoEffect.SetWallpaperIntent -> {
                    context.startActivity(effect.intent)
                }
                is PhotoEffect.WallpaperSetFailed -> {
                    Toast.makeText(context, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    KonaTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                Column {
                    AnimatedVisibility(
                        visible = state.isOverlayVisible,
                        enter = slideInHorizontally(initialOffsetX = { it }),
                        exit = slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        FloatingActionButton(
                            onClick = { viewModel.handleIntent(PhotoIntent.Download) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download"
                            )
                        }
                    }
                    
                    AnimatedVisibility(
                        visible = state.isOverlayVisible,
                        enter = slideInHorizontally(initialOffsetX = { it }),
                        exit = slideOutHorizontally(targetOffsetX = { it })
                    ) {
                        FloatingActionButton(
                            onClick = { viewModel.handleIntent(PhotoIntent.SetWallpaper) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wallpaper,
                                contentDescription = "Set as Wallpaper"
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            PhotoContent(
                paddingValues = paddingValues,
                state = state,
                onToggleOverlay = { viewModel.handleIntent(PhotoIntent.ToggleOverlay) },
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoContent(
    paddingValues: PaddingValues,
    state: PhotoState,
    onToggleOverlay: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Photo(
            state = state,
            onPhotoClick = onToggleOverlay
        )
        
        TopBar(
            isVisible = state.isOverlayVisible,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.TopBar(
    isVisible: Boolean,
    navController: NavController
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier.align(Alignment.TopCenter),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                // IconButton(onClick = { /* TODO: 收藏功能 */ }) {
                //     Icon(
                //         imageVector = Icons.Default.Favorite,
                //         contentDescription = "Favorite"
                //     )
                // }
                // IconButton(onClick = { /* TODO: 分享功能 */ }) {
                //     Icon(
                //         imageVector = Icons.Default.Share,
                //         contentDescription = "Share"
                //     )
                // }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
            )
        )
    }
}

@Composable
private fun Photo(
    state: PhotoState,
    onPhotoClick: () -> Unit
) {
    val cover = state.cover ?: return
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offsetX += pan.x
                    /** scale*/
                    offsetY += pan.y
                    /** scale*/
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onPhotoClick() }
                )
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cover.sampleUrl)
                .crossfade(true)
                .build(),
            loading = {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            contentDescription = cover.title,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                ),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoPreview() {
    val sampleState = PhotoState(
        cover = Cover(
            id = "preview",
            title = "Preview Image",
            previewUrl = "https://picsum.photos/800/600"
        )
    )
    
    KonaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Photo(
                state = sampleState,
                onPhotoClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoContentPreview() {
    val sampleState = PhotoState(
        cover = Cover(
            id = "preview",
            title = "Preview Image",
            previewUrl = "https://picsum.photos/800/600"
        )
    )
    
    KonaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TopBar(
                isVisible = true,
                navController = rememberNavController()
            )
        }
    }
}