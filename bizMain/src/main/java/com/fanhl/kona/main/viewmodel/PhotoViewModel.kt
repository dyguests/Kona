package com.fanhl.kona.main.viewmodel

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
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
        val fileName = downloadManager.generateFileName(cover.id)

        viewModelScope.launch {
            try {
                // First download the image
                if (!downloadManager.isFileExists(fileName)) {
                    downloadManager.downloadFile(url, fileName)
                }
                
                // Then set it as wallpaper using Intent
                val file = downloadManager.getFile(fileName)
                val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    setDataAndType(android.net.Uri.fromFile(file), "image/jpeg")
                    putExtra("mimeType", "image/jpeg")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                setEffect { PhotoEffect.SetWallpaperIntent(intent) }
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
    data class SetWallpaperIntent(val intent: Intent) : PhotoEffect()
    object WallpaperSetFailed : PhotoEffect()
} 