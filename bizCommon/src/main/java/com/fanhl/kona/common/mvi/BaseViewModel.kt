package com.fanhl.kona.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class implementing MVI architecture
 * @param UiIntent The type of user intents/actions
 * @param UiState The type of UI state
 * @param UiEffect The type of one-time effects
 */
abstract class BaseViewModel<UiIntent : IUiIntent, UiState : IUiState, UiEffect : IUiEffect> : ViewModel() {

    private val initialState: UiState by lazy { createInitialState() }
    abstract fun createInitialState(): UiState

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _effect = Channel<UiEffect>()
    val effect = _effect.receiveAsFlow()

    abstract fun handleIntent(intent: UiIntent)

    /**
     * Set new UI state
     */
    protected fun setState(reduce: UiState.() -> UiState) {
        val newState = uiState.value.reduce()
        _uiState.value = newState
    }

    /**
     * Trigger one-time effect
     */
    protected fun setEffect(builder: () -> UiEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
} 