package com.lin.framework.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface IUiIntent
interface IUiState
interface IUiEffect

abstract class IseViewModel<UiIntent : IUiIntent, UiState : IUiState, UiEffect : IUiEffect> : BaseViewModel() {
    private val _uiIntentFlow: Channel<UiIntent> = Channel()
    val uiIntentFlow = _uiIntentFlow.receiveAsFlow()

    protected abstract fun initUiState(): UiState
    protected fun sendState(copier: UiState.() -> UiState) = _uiStateFlow.update { copier(_uiStateFlow.value) }

    private val _uiStateFlow by lazy { MutableStateFlow(initUiState()) }
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _effectFlow = MutableSharedFlow<UiEffect>()
    val uiEffectFlow = _effectFlow.asSharedFlow()

    fun sendEffect(builder: suspend () -> UiEffect) = viewModelScope.launch { _effectFlow.emit(builder()) }
    protected suspend fun sendEffect(effect: UiEffect) = _effectFlow.emit(effect)
}