package com.fanhl.kona.main.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fanhl.kona.common.ui.theme.KonaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    KonaTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            MainContent(
                innerPadding = innerPadding,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    innerPadding: PaddingValues,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState { 3 }
    val listState = rememberLazyStaggeredGridState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    GalleryPage(
                        innerPadding = innerPadding,
                        navController = navController,
                        listState = listState
                    )
                }

                1 -> {
                    SearchPage()
                }

                2 -> {
                    ProfilePage()
                }
            }
        }

        BottomBar(
            listState = listState,
            selectedIndex = pagerState.currentPage,
            onSelectedIndexChange = {
                scope.launch {
                    pagerState.scrollToPage(it)
                }
            },
            navController = navController
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BoxScope.BottomBar(
    listState: LazyStaggeredGridState,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    navController: NavController
) {
    AnimatedVisibility(
        visible = !listState.isScrollInProgress,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            NavigationBar(
                modifier = Modifier.size(240.dp, 48.dp),
                containerColor = Color.Transparent,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    selected = selectedIndex == 0,
                    onClick = { onSelectedIndexChange(0) }
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    selected = selectedIndex == 1,
                    onClick = { onSelectedIndexChange(1) }
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    selected = selectedIndex == 2,
                    onClick = { onSelectedIndexChange(2) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainContentPreview() {
    KonaTheme {
        MainContent(
            innerPadding = PaddingValues(),
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    KonaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomBar(listState = rememberLazyStaggeredGridState(), selectedIndex = 0, onSelectedIndexChange = {}, navController = rememberNavController())
        }
    }
}

