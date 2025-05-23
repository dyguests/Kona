package com.fanhl.kona.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import com.fanhl.kona.database.dao.QueryDao
import com.fanhl.kona.database.entity.QueryEntity
import com.fanhl.kona.main.usecase.GetCoversUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getCoversUseCase: GetCoversUseCase,
    private val queryDao: QueryDao
) : BaseViewModel<GalleryIntent, GalleryState, GalleryEffect>() {

    override fun createInitialState(): GalleryState = GalleryState()

    init {
        // 收集最近的搜索记录
        viewModelScope.launch {
            queryDao.getRecentQueries().collectLatest { queries ->
                setState { copy(recentQueries = queries) }
                // 如果有最近的搜索记录，设置为当前查询
                queries.firstOrNull()?.let { latestQuery ->
                    setState { copy(searchQuery = latestQuery.query) }
                }
            }
        }
    }

    override fun handleIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.UpdateSearchQuery -> {
                setState { copy(searchQuery = intent.query) }
                // 保存搜索记录
                viewModelScope.launch {
                    queryDao.insert(
                        QueryEntity(
                            query = intent.query,
                            lastUsedAt = java.time.Instant.now(),
                            useCount = 1
                        )
                    )
                }
                refresh()
            }
            is GalleryIntent.Refresh -> {
                refresh()
            }
            is GalleryIntent.LoadMore -> {
                loadMore()
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true) }
            try {
                val covers = getCoversUseCase.execute(uiState.value.searchQuery, 1)
                setState { 
                    copy(
                        covers = covers,
                        currentPage = 1
                    ) 
                }
                setEffect { GalleryEffect.RefreshSuccess }
            } catch (e: Exception) {
                setEffect { GalleryEffect.RefreshError(e.message ?: "Refresh failed") }
            } finally {
                setState { copy(isRefreshing = false) }
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            setState { copy(isLoadingMore = true) }
            try {
                val nextPage = uiState.value.currentPage + 1
                val newCovers = getCoversUseCase.execute(uiState.value.searchQuery, nextPage)
                setState { 
                    copy(
                        covers = covers + newCovers,
                        currentPage = nextPage
                    ) 
                }
                setEffect { GalleryEffect.LoadMoreSuccess }
            } catch (e: Exception) {
                setEffect { GalleryEffect.LoadMoreError(e.message ?: "Load more failed") }
            } finally {
                setState { copy(isLoadingMore = false) }
            }
        }
    }
}

// Define your MVI components
sealed class GalleryIntent : IUiIntent {
    data class UpdateSearchQuery(val query: String) : GalleryIntent()
    object Refresh : GalleryIntent()
    object LoadMore : GalleryIntent()
}

data class GalleryState(
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1,
    val covers: List<Cover> = emptyList(),
    val recentQueries: List<QueryEntity> = emptyList()
) : IUiState

sealed class GalleryEffect : IUiEffect {
    object RefreshSuccess : GalleryEffect()
    data class RefreshError(val message: String) : GalleryEffect()
    object LoadMoreSuccess : GalleryEffect()
    data class LoadMoreError(val message: String) : GalleryEffect()
} 