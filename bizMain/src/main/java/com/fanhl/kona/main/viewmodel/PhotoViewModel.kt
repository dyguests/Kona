package com.fanhl.kona.main.viewmodel

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.viewModelScope
import com.fanhl.kona.common.entity.Cover
import com.fanhl.kona.common.mvi.BaseViewModel
import com.fanhl.kona.common.mvi.IUiEffect
import com.fanhl.kona.common.mvi.IUiIntent
import com.fanhl.kona.common.mvi.IUiState
import com.fanhl.kona.common.util.DownloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadManager: DownloadManager,
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
            is PhotoIntent.SetWallpaper -> {
                handleSetWallpaper()
            }
        }
    }

    private fun handleDownload() {
        val cover = uiState.value.cover ?: return
        val url = cover.jpegUrl ?: return
        val fileName = "Kona_${cover.id}.jpg"
        
        if (downloadManager.isFileExists(fileName)) {
            setEffect { PhotoEffect.FileExists }
        } else {
            downloadManager.downloadFile(url, fileName)
            setEffect { PhotoEffect.DownloadStarted }
        }
    }

    private fun handleSetWallpaper() {
        val cover = uiState.value.cover ?: return
        val url = cover.jpegUrl ?: return
        val fileName = "Kona_${cover.id}.jpg"

        viewModelScope.launch {
            try {
                // First download the image
                if (!downloadManager.isFileExists(fileName)) {
                    downloadManager.downloadFile(url, fileName)
                }
                
                // Then set it as wallpaper
                val bitmap = withContext(Dispatchers.IO) {
                    val file = downloadManager.getFile(fileName)
                    BitmapFactory.decodeFile(file.absolutePath)
                }
                val wallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                setEffect { PhotoEffect.WallpaperSet }
            } catch (e: Exception) {
                setEffect { PhotoEffect.WallpaperSetFailed }
            }
        }
    }
}

// Define your MVI components
sealed class PhotoIntent : IUiIntent {
    data class SetCover(val cover: Cover) : PhotoIntent()
    object ToggleOverlay : PhotoIntent()
    object Download : PhotoIntent()
    object SetWallpaper : PhotoIntent()
}

data class PhotoState(
    val cover: Cover? = null,
    val isOverlayVisible: Boolean = false
) : IUiState

sealed class PhotoEffect : IUiEffect {
    object DownloadStarted : PhotoEffect()
    object FileExists : PhotoEffect()
    object WallpaperSet : PhotoEffect()
    object WallpaperSetFailed : PhotoEffect()
} 