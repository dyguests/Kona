package com.fanhl.kona.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainIntent, MainState, MainEffect>() {

    override fun createInitialState(): MainState = MainState()

    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateSearchQuery -> {
                setState { copy(searchQuery = intent.query) }
            }
            is MainIntent.Refresh -> {
                refresh()
            }
            is MainIntent.LoadMore -> {
                loadMore()
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true) }
            try {
                // TODO: Implement refresh logic
                setEffect { MainEffect.RefreshSuccess }
            } catch (e: Exception) {
                setEffect { MainEffect.RefreshError(e.message ?: "Refresh failed") }
            } finally {
                setState { copy(isRefreshing = false) }
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            setState { copy(isLoadingMore = true) }
            try {
                // TODO: Implement load more logic
                setEffect { MainEffect.LoadMoreSuccess }
            } catch (e: Exception) {
                setEffect { MainEffect.LoadMoreError(e.message ?: "Load more failed") }
            } finally {
                setState { copy(isLoadingMore = false) }
            }
        }
    }
}

// Define your MVI components
sealed class MainIntent : IUiIntent {
    data class UpdateSearchQuery(val query: String) : MainIntent()
    object Refresh : MainIntent()
    object LoadMore : MainIntent()
}

data class MainState(
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
) : IUiState

sealed class MainEffect : IUiEffect {
    object RefreshSuccess : MainEffect()
    data class RefreshError(val message: String) : MainEffect()
    object LoadMoreSuccess : MainEffect()
    data class LoadMoreError(val message: String) : MainEffect()
} 