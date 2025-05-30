package com.fanhl.kona.main.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.fanhl.kona.main.navigation.NavRoutes
import com.fanhl.kona.main.viewmodel.PhotoEffect
import com.fanhl.kona.main.viewmodel.PhotoIntent
import com.fanhl.kona.main.viewmodel.PhotoState
import com.fanhl.kona.main.viewmodel.PhotoViewModel
import com.fanhl.kona.main.compose.rememberPhotoState

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

        PhotoTags(
            state = state,
            navController = navController,
            paddingValues = paddingValues
        )
        
        TopBar(
            isVisible = state.isOverlayVisible,
            navController = navController
        )
    }
}

@Composable
private fun BoxScope.PhotoTags(
    state: PhotoState,
    navController: NavController,
    paddingValues: PaddingValues
) {
    AnimatedVisibility(
        visible = state.isOverlayVisible,
        modifier = Modifier
            .align(Alignment.BottomStart)
            // .padding(8.dp)
            .padding(end = 64.dp)  // 留出 FAB 的空间
            .padding(paddingValues)
            .fillMaxWidth(),
    ) {
        FlowRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            // verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.cover?.tags?.forEach { tag ->
                FilterChip(
                    selected = false,
                    onClick = { 
                        navController.previousBackStackEntry?.savedStateHandle?.set(NavRoutes.Args.TAGS, tag)
                        navController.navigateUp()
                    },
                    label = {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
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
    var photoState by rememberPhotoState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    photoState = photoState.copy(
                        scale = photoState.scale * zoom,
                        offsetX = photoState.offsetX + pan.x,
                        offsetY = photoState.offsetY + pan.y
                    )
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(cover.previewUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = cover.title,
                        modifier = Modifier.fillMaxSize(),
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            contentDescription = cover.title,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = photoState.scale,
                    scaleY = photoState.scale,
                    translationX = photoState.offsetX,
                    translationY = photoState.offsetY
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