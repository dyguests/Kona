package com.fanhl.kona.main.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
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
import com.fanhl.kona.main.entity.SiteType
import com.fanhl.kona.main.navigation.NavRoutes
import com.fanhl.kona.main.viewmodel.GalleryEffect
import com.fanhl.kona.main.viewmodel.GalleryIntent
import com.fanhl.kona.main.viewmodel.GalleryState
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

    LaunchedEffect(Unit) {
        if (uiState.covers.isEmpty()) {
            viewModel.handleIntent(GalleryIntent.Refresh)
        }
    }

    // Handle tag selection from PhotoScreen
    LaunchedEffect(Unit) {
        val selectedTag = navController.currentBackStackEntry?.savedStateHandle?.get<String>(NavRoutes.Args.TAGS)
        selectedTag?.let { tag ->
            // 更新搜索框并执行搜索
            viewModel.handleIntent(GalleryIntent.UpdateSearchInput(tag))
            viewModel.handleIntent(GalleryIntent.Search(tag))
            // 清除保存的标签，避免重复触发
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>(NavRoutes.Args.TAGS)
        }
    }

    // Effect collection
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GalleryEffect.LoadSuccess -> {
                    // Handle load success
                }
                is GalleryEffect.LoadError -> {
                    // Handle load error
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
            uiState = uiState,
            onRefresh = { viewModel.handleIntent(GalleryIntent.Refresh) },
            navController = navController
        )
        TopBar(
            listState = listState,
            uiState = uiState,
            onSearchQueryChange = { query ->
                viewModel.handleIntent(GalleryIntent.UpdateSearchInput(query))
            },
            onSearch = { query ->
                viewModel.handleIntent(GalleryIntent.Search(query))
            },
            onClearSearch = { viewModel.handleIntent(GalleryIntent.ClearSearch) },
            onSiteChange = { siteType ->
                viewModel.handleIntent(GalleryIntent.UpdateSiteType(siteType))
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
    uiState: GalleryState,
    onRefresh: () -> Unit = {},
    navController: NavController
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.Companion
            .fillMaxSize(),
        state = pullRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier
                    .align(Alignment.Companion.TopCenter)
                    .padding(innerPadding)
                    .padding(PaddingValues(top = 64.dp)),
                isRefreshing = uiState.isRefreshing,
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
            items(uiState.covers) { cover ->
                CoverItem(
                    cover = cover,
                    navController = navController
                )
            }

            if (uiState.isLoadingMore) {
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
    uiState: GalleryState,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSiteChange: (SiteType) -> Unit,
    navController: NavController
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    var isShowingIconSuggestions by remember { mutableStateOf(false) }
    val horizontalPadding by animateDpAsState(
        targetValue = if (!isSearchExpanded) 8.dp else 0.dp,
        label = "horizontalPadding"
    )

    AnimatedVisibility(
        visible = !listState.isScrollInProgress,
        modifier = Modifier.align(Alignment.TopCenter),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = { query ->
                        onSearch(query)
                        isSearchExpanded = false
                        isShowingIconSuggestions = false
                    },
                    expanded = isSearchExpanded,
                    onExpandedChange = { 
                        isSearchExpanded = it
                        if (it && !isShowingIconSuggestions) {
                            isShowingIconSuggestions = false
                        } else if (!it) {
                            isShowingIconSuggestions = false
                        }
                    },
                    placeholder = { Text("搜索") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(uiState.siteType.icon),
                            contentDescription = "搜索",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isSearchExpanded = true
                                    isShowingIconSuggestions = true
                                },
                            tint = Color.Unspecified,
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onClearSearch() }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "清除搜索"
                                )
                            }
                        }
                    }
                )
            },
            expanded = isSearchExpanded,
            onExpandedChange = {
                isSearchExpanded = it
                if (it && !isShowingIconSuggestions) {
                    isShowingIconSuggestions = false
                } else if (!it) {
                    isShowingIconSuggestions = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .align(Alignment.TopCenter)
        ) {
            if (isShowingIconSuggestions) {
                // 搜索图标点击后的建议列表
                LazyColumn {
                    items(SiteType.values()) { siteType ->
                        ListItem(
                            headlineContent = { Text(siteType.displayName) },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(siteType.icon),
                                    contentDescription = siteType.displayName,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified,
                                )
                            },
                            modifier = Modifier.clickable {
                                onSiteChange(siteType)
                                isSearchExpanded = false
                                isShowingIconSuggestions = false
                            }
                        )
                    }
                }
            } else {
                // 原有的搜索历史建议
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
                                onSearch(query.query)
                                isSearchExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WaterfallGridPreview() {
    KonaTheme {
        WaterfallGrid(
            innerPadding = PaddingValues(),
            listState = rememberLazyStaggeredGridState(),
            uiState = GalleryState(),
            onRefresh = {},
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
                uiState = GalleryState(),
                onSearchQueryChange = {},
                onSearch = {},
                onClearSearch = {},
                onSiteChange = {},
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