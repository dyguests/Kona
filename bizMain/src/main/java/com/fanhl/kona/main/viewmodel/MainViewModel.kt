package com.fanhl.kona.main.viewmodel

import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainIntent, MainState, MainEffect>() {

    override fun createInitialState(): MainState = MainState()

    override fun handleIntent(intent: MainIntent) {
        when (intent) {
            // Handle intents here
        }
    }
}

// Define your MVI components
sealed class MainIntent : IUiIntent {
    // Add your intents here
}

data class MainState(
    // Add your state properties here
) : IUiState

sealed class MainEffect : IUiEffect {
    // Add your effects here
} 