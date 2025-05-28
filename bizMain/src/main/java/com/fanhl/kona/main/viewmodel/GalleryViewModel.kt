package com.fanhl.kona.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import com.fanhl.kona.database.dao.QueryDao
import com.fanhl.kona.database.entity.QueryEntity
import com.fanhl.kona.main.entity.SiteType
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

    override fun handleIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.Init -> handleInit()
            is GalleryIntent.UpdateSearchInput -> setState { copy(searchQuery = intent.query) }
            is GalleryIntent.Search -> handleSearch(intent.query)
            is GalleryIntent.ClearSearch -> {
                setState { copy(searchQuery = "") }
                refresh()
            }
            is GalleryIntent.Refresh -> refresh()
            is GalleryIntent.LoadMore -> loadMore()
            is GalleryIntent.UpdateSiteType -> {
                setState { copy(siteType = intent.siteType) }
                refresh()
            }
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            queryDao.getRecentQueries().collectLatest { queries ->
                setState { copy(recentQueries = queries) }
                queries.firstOrNull()?.let { latestQuery ->
                    setState { copy(searchQuery = latestQuery.query) }
                    refresh()
                }
            }
        }
    }

    private fun handleSearch(query: String) {
        setState { copy(searchQuery = query) }
        viewModelScope.launch {
            queryDao.updateOrInsertQuery(query)
        }
        refresh()
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
    object Init : GalleryIntent()
    data class UpdateSearchInput(val query: String) : GalleryIntent()
    data class Search(val query: String) : GalleryIntent()
    object ClearSearch : GalleryIntent()
    object Refresh : GalleryIntent()
    object LoadMore : GalleryIntent()
    data class UpdateSiteType(val siteType: SiteType) : GalleryIntent()
}

data class GalleryState(
    val siteType: SiteType = SiteType.Konachan,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1,
    val covers: List<Cover> = emptyList(),
    val recentQueries: List<QueryEntity> = emptyList(),
) : IUiState

sealed class GalleryEffect : IUiEffect {
    object RefreshSuccess : GalleryEffect()
    data class RefreshError(val message: String) : GalleryEffect()
    object LoadMoreSuccess : GalleryEffect()
    data class LoadMoreError(val message: String) : GalleryEffect()
} 