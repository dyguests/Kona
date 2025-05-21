package com.fanhl.kona.main.viewmodel

import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import com.fanhl.kona.common.util.DownloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val downloadManager: DownloadManager
) : BaseViewModel<PhotoIntent, PhotoState, PhotoEffect>() {
    override fun createInitialState(): PhotoState = PhotoState()

    override fun handleIntent(intent: PhotoIntent) {
        when (intent) {
            is PhotoIntent.SetCover -> {
                setState { copy(cover = intent.cover) }
            }
            is PhotoIntent.ToggleOverlay -> {
                setState { copy(isOverlayVisible = !isOverlayVisible) }
            }
            is PhotoIntent.Download -> {
                handleDownload()
            }
        }
    }

    private fun handleDownload() {
        val cover = uiState.value.cover ?: return
        val url = cover.jpegUrl ?: return
        val fileName = "Kona_${cover.id}.jpg"
        downloadManager.downloadFile(url, fileName)
        setEffect { PhotoEffect.DownloadStarted }
    }
}

// Define your MVI components
sealed class PhotoIntent : IUiIntent {
    data class SetCover(val cover: Cover) : PhotoIntent()
    object ToggleOverlay : PhotoIntent()
    object Download : PhotoIntent()
}

data class PhotoState(
    val cover: Cover? = null,
    val isOverlayVisible: Boolean = false
) : IUiState

sealed class PhotoEffect : IUiEffect {
    object DownloadStarted : PhotoEffect()
} 