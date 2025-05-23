package com.fanhl.kona.main.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.fanhl.kona.main.viewmodel.GalleryEffect
import com.fanhl.kona.main.viewmodel.GalleryIntent
import com.fanhl.kona.main.viewmodel.GalleryViewModel
import com.fanhl.util.plus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GalleryPage(
    innerPadding: PaddingValues,
    navController: NavController,
    listState: LazyStaggeredGridState
) {
    val viewModel: GalleryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Initial refresh
    LaunchedEffect(Unit) {
        viewModel.handleIntent(GalleryIntent.Refresh)
    }

    // Effect collection
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GalleryEffect.RefreshSuccess -> {
                    // Handle refresh success
                }
                is GalleryEffect.RefreshError -> {
                    // Handle refresh error
                }
                is GalleryEffect.LoadMoreSuccess -> {
                    // Handle load more success
                }
                is GalleryEffect.LoadMoreError -> {
                    // Handle load more error
                }
            }
        }
    }

    // Handle load more
    val loadMoreThreshold = 5
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleItem >= totalItems - loadMoreThreshold
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !uiState.isLoadingMore) {
            viewModel.handleIntent(GalleryIntent.LoadMore)
        }
    }

    Box(
        modifier = Modifier.Companion.fillMaxSize(),
    ) {
        WaterfallGrid(
            innerPadding = innerPadding,
            listState = listState,
            covers = uiState.covers,
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.handleIntent(GalleryIntent.Refresh) },
            isLoadingMore = uiState.isLoadingMore,
            navController = navController
        )
        TopBar(
            listState = listState,
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = { query ->
                viewModel.handleIntent(GalleryIntent.UpdateSearchQuery(query))
            },
            navController = navController
        )
    }
}

@Composable
private fun CoverItem(
    cover: Cover,
    navController: NavController
) {
    val aspectRatio = if (cover.previewWidth != null && cover.previewHeight != null) {
        (cover.previewWidth!!.toFloat() / cover.previewHeight!!.toFloat()).coerceIn(0.5f, 2f)
    } else {
        1f
    }

    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable {
                // 点击跳转到详情页
                navController.navigate(NavRoutes.PHOTO) {
                    popUpTo(NavRoutes.GALLERY)
                    launchSingleTop = true
                    restoreState = true
                }
                navController.getBackStackEntry(NavRoutes.PHOTO).savedStateHandle[NavRoutes.Args.COVER] = cover
            }
    ) {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            contentAlignment = Alignment.Companion.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cover.previewUrl)
                    .crossfade(true)
                    .build(),
                loading = {
                    Box(
                        contentAlignment = Alignment.Companion.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.Companion.size(48.dp)
                        )
                    }
                },
                contentDescription = cover.title,
                modifier = Modifier.Companion.fillMaxSize(),
                contentScale = ContentScale.Companion.Crop
            )
            if (false && cover.title != null) {
                Text(
                    text = cover.title ?: "",
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.TopStart)
                        .background(
                            color = Color.Companion.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(bottomEnd = 8.dp)
                        )
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterfallGrid(
    innerPadding: PaddingValues,
    listState: LazyStaggeredGridState,
    covers: List<Cover>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    isLoadingMore: Boolean = false,
    navController: NavController
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.Companion
            .fillMaxSize(),
        state = pullRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.Companion.align(Alignment.Companion.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        },
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp) + innerPadding + PaddingValues(
                top = 64.dp,  // TopAppBar height
                bottom = 80.dp // NavigationBar height
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            state = listState,
            modifier = Modifier.Companion.fillMaxSize()
        ) {
            items(covers) { cover ->
                CoverItem(
                    cover = cover,
                    navController = navController
                )
            }

            if (isLoadingMore) {
                item(span = StaggeredGridItemSpan.Companion.FullLine) {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BoxScope.TopBar(
    listState: LazyStaggeredGridState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController
) {
    val viewModel: GalleryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSearchExpanded by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = !listState.isScrollInProgress,
        modifier = Modifier.Companion.align(Alignment.Companion.TopCenter),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = { onSearchQueryChange(it) },
                    active = isSearchExpanded,
                    onActiveChange = { isSearchExpanded = it },
                    placeholder = { Text("搜索") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "清除搜索"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-4).dp),
                ) {
                    // 显示搜索建议
                    LazyColumn {
                        items(uiState.recentQueries) { query ->
                            ListItem(
                                headlineContent = { Text(query.query) },
                                supportingContent = { 
                                    Text(
                                        "使用次数: ${query.useCount}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                modifier = Modifier.clickable {
                                    onSearchQueryChange(query.query)
                                    isSearchExpanded = false
                                }
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.Companion
                        .padding(8.dp)
                        .size(48.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(NavRoutes.MENU)
                        },
                        modifier = Modifier.Companion.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "菜单"
                        )
                    }
                }
            },
            actions = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.Companion
                        .padding(8.dp)
                        .size(48.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(NavRoutes.NOTIFICATIONS)
                        },
                        modifier = Modifier.Companion.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "通知"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WaterfallGridPreview() {
    KonaTheme {
        WaterfallGrid(
            innerPadding = PaddingValues(),
            listState = rememberLazyStaggeredGridState(),
            covers = List(10) { index ->
                Cover(
                    id = index.toString(),
                    title = "Cover $index"
                )
            },
            isRefreshing = false,
            isLoadingMore = false,
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    KonaTheme {
        Box(modifier = Modifier.Companion.fillMaxSize()) {
            TopBar(
                listState = rememberLazyStaggeredGridState(),
                searchQuery = "Search query",
                onSearchQueryChange = {},
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoverItemPreview() {
    KonaTheme {
        CoverItem(
            cover = Cover(
                id = "1",
                title = "Sample Cover",
                previewUrl = "https://picsum.photos/200/300"
            ),
            navController = rememberNavController()
        )
    }
}