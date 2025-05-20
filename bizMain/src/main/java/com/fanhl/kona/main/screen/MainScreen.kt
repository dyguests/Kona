package com.fanhl.kona.main.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.ui.theme.KonaTheme
import com.fanhl.kona.main.viewmodel.MainEffect
import com.fanhl.kona.main.viewmodel.MainIntent
import com.fanhl.kona.main.viewmodel.MainState
import com.fanhl.kona.main.viewmodel.MainViewModel
import com.fanhl.util.plus
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Initial refresh
    LaunchedEffect(Unit) {
        viewModel.handleIntent(MainIntent.Refresh)
    }

    // Effect collection
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainEffect.RefreshSuccess -> {
                    // Handle refresh success
                }
                is MainEffect.RefreshError -> {
                    // Handle refresh error
                }
                is MainEffect.LoadMoreSuccess -> {
                    // Handle load more success
                }
                is MainEffect.LoadMoreError -> {
                    // Handle load more error
                }
            }
        }
    }

    KonaTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            MainContent(
                innerPadding = innerPadding,
                uiState = uiState,
                onSearchQueryChange = { query ->
                    viewModel.handleIntent(MainIntent.UpdateSearchQuery(query))
                },
                onRefresh = { viewModel.handleIntent(MainIntent.Refresh) },
                onLoadMore = { viewModel.handleIntent(MainIntent.LoadMore) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    innerPadding: PaddingValues,
    uiState: MainState,
    onSearchQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyStaggeredGridState()

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
            onLoadMore()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        WaterfallGrid(
            innerPadding = innerPadding,
            listState = listState,
            covers = uiState.covers,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            isLoadingMore = uiState.isLoadingMore
        )
        
        TopBar(
            listState = listState,
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = onSearchQueryChange
        )
        BottomBar(listState)
    }
}

@Composable
private fun CoverItem(cover: Cover) {
    val aspectRatio = if (cover.previewWidth != null && cover.previewHeight != null) {
        (cover.previewWidth!!.toFloat() / cover.previewHeight!!.toFloat()).coerceIn(0.5f, 2f)
    } else {
        1f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cover.previewUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = cover.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            if (false&&cover.title != null) {
                Text(
                    text = cover.title ?: "",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
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
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize(),
        state = pullRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
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
            modifier = Modifier.fillMaxSize()
        ) {
            items(covers) { cover ->
                CoverItem(cover = cover)
            }

            if (isLoadingMore) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
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
    onSearchQueryChange: (String) -> Unit
) {
    AnimatedVisibility(
        visible = !listState.isScrollInProgress,
        modifier = Modifier.align(Alignment.TopCenter),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = {
                SearchBar(
                    inputField = {
                        TextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("Search") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 0.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            )
                        )
                    },
                    expanded = false,
                    onExpandedChange = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-4).dp),
                ) {
                    // Search suggestions can be added here
                }
            },
            navigationIcon = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                ) {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            },
            actions = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                ) {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
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

@Composable
private fun BoxScope.BottomBar(listState: LazyStaggeredGridState) {
    AnimatedVisibility(
        visible = !listState.isScrollInProgress,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = true,
                onClick = { }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") },
                selected = false,
                onClick = { }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = false,
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainContentPreview() {
    KonaTheme {
        MainContent(
            innerPadding = PaddingValues(),
            uiState = MainState(
                covers = List(10) { index ->
                    Cover(
                        id = index.toString(),
                        title = "Cover $index"
                    )
                },
                searchQuery = "",
                isRefreshing = false,
                isLoadingMore = false
            ),
            onSearchQueryChange = {},
            onRefresh = {},
            onLoadMore = {}
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
            isLoadingMore = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    KonaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TopBar(
                listState = rememberLazyStaggeredGridState(),
                searchQuery = "Search query"
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    KonaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomBar(listState = rememberLazyStaggeredGridState())
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
            )
        )
    }
}
