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
import com.fanhl.util.sp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getCoversUseCase: GetCoversUseCase,
    private val queryDao: QueryDao
) : BaseViewModel<GalleryIntent, GalleryState, GalleryEffect>() {
    private var cacheSiteType by sp<SiteType>("gallery_site_type")
    private var cacheSearchQuery by sp<String>("gallery_search_query")

    override fun createInitialState(): GalleryState = GalleryState(
        siteType = this@GalleryViewModel.cacheSiteType ?: SiteType.Konachan,
        searchQuery = this@GalleryViewModel.cacheSearchQuery ?: "",
    )

    override fun handleIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.Init -> handleInit()
            is GalleryIntent.UpdateSearchInput -> setState { copy(searchQuery = intent.query) }
            is GalleryIntent.Search -> handleSearch(intent.query)
            is GalleryIntent.ClearSearch -> handleSearch("")
            is GalleryIntent.Refresh -> refresh()
            is GalleryIntent.LoadMore -> loadMore()
            is GalleryIntent.UpdateSiteType -> updateSite(intent)
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            queryDao.getRecentQueries().collectLatest { queries ->
                setState { copy(recentQueries = queries) }
            }
        }
        viewModelScope.launch {
            refresh()
        }
    }

    private fun handleSearch(query: String) {
        setState { copy(searchQuery = query) }
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                queryDao.updateOrInsertQuery(query)
            }
        }
        cacheSearchQuery = query
        refresh()
    }

    private fun refresh() {
        loadData(isLoadMore = false)
    }

    private fun loadMore() {
        loadData(isLoadMore = true)
    }

    private fun loadData(isLoadMore: Boolean) {
        viewModelScope.launch {
            if (isLoadMore) {
                setState { copy(isLoadingMore = true) }
            } else {
                setState { copy(isRefreshing = true) }
            }
            
            try {
                val page = if (isLoadMore) uiState.value.currentPage + 1 else 1
                val newCovers = getCoversUseCase.execute(uiState.value.searchQuery, page)
                
                setState { 
                    copy(
                        covers = if (isLoadMore) covers + newCovers else newCovers,
                        currentPage = page
                    ) 
                }
                
                setEffect { GalleryEffect.LoadSuccess }
            } catch (e: Exception) {
                setEffect { GalleryEffect.LoadError(e.message ?: "Load failed") }
            } finally {
                setState { 
                    if (isLoadMore) copy(isLoadingMore = false)
                    else copy(isRefreshing = false)
                }
            }
        }
    }

    private fun updateSite(intent: GalleryIntent.UpdateSiteType) {
        cacheSiteType = intent.siteType
        setState { copy(siteType = intent.siteType) }
        refresh()
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
    object LoadSuccess : GalleryEffect()
    data class LoadError(val message: String) : GalleryEffect()
} 