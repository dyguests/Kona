package com.fanhl.kona.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import com.fanhl.kona.main.usecase.GetCoversUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCoversUseCase: GetCoversUseCase
) : BaseViewModel<MainIntent, MainState, MainEffect>() {

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
                val covers = getCoversUseCase.execute("landscape")
                setState { copy(covers = covers) }
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
                val newCovers = getCoversUseCase.execute("landscape")
                setState { copy(covers = covers + newCovers) }
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
    val covers: List<Cover> = emptyList(),
) : IUiState

sealed class MainEffect : IUiEffect {
    object RefreshSuccess : MainEffect()
    data class RefreshError(val message: String) : MainEffect()
    object LoadMoreSuccess : MainEffect()
    data class LoadMoreError(val message: String) : MainEffect()
} 