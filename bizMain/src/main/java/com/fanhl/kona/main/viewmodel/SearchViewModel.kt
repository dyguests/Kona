package com.fanhl.kona.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchState(
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

sealed class SearchIntent {
    data class UpdateSearchQuery(val query: String) : SearchIntent()
    object Search : SearchIntent()
}

sealed class SearchEffect {
    object SearchSuccess : SearchEffect()
    data class SearchError(val error: String) : SearchEffect()
}

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    private val _effect = MutableStateFlow<SearchEffect?>(null)
    val effect: StateFlow<SearchEffect?> = _effect.asStateFlow()

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateSearchQuery -> {
                _uiState.value = _uiState.value.copy(searchQuery = intent.query)
            }
            is SearchIntent.Search -> {
                // TODO: Implement search logic
            }
        }
    }
} 