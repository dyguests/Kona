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

interface IUiState
interface IUiIntent
interface IUiEffect

abstract class MviViewModel<UiState : IUiState, UiIntent : IUiIntent, UiEffect : IUiEffect> : BaseViewModel() {
    private val _uiStateFlow by lazy { MutableStateFlow(initUiState()) }
    val uiStateFlow = _uiStateFlow.asStateFlow()

    protected abstract fun initUiState(): UiState
    protected fun sendState(copier: UiState.() -> UiState) = _uiStateFlow.update { copier(_uiStateFlow.value) }

    private val _uiIntentFlow: Channel<UiIntent> = Channel()
    private val uiIntentFlow = _uiIntentFlow.receiveAsFlow()

    fun sendIntent(uiIntent: UiIntent) = viewModelScope.launch { _uiIntentFlow.send(uiIntent) }
    protected abstract fun handleIntent(intent: UiIntent)

    private val _uiEffectFlow = MutableSharedFlow<UiEffect>()
    val uiEffectFlow = _uiEffectFlow.asSharedFlow()

    protected fun sendEffect(builder: suspend () -> UiEffect) = viewModelScope.launch { _uiEffectFlow.emit(builder()) }
    protected suspend fun sendEffect(effect: UiEffect) = _uiEffectFlow.emit(effect)

    init {
        viewModelScope.launch {
            uiIntentFlow.collect {
                handleIntent(it)
            }
        }
    }
}