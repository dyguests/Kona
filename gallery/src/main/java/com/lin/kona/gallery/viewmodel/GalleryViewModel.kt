package com.lin.kona.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.lin.framework.base.IUiEffect
import com.lin.framework.base.IUiIntent
import com.lin.framework.base.IUiState
import com.lin.framework.base.MviViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// @HiltViewModel
class GalleryViewModel @Inject constructor(

) : MviViewModel<GalleryUiState, GalleryUiIntent, GalleryUiEffect>() {
    override fun initUiState() = GalleryUiState()

    override fun handleIntent(intent: GalleryUiIntent) = when (intent) {
        GalleryUiIntent.LoadGallery -> loadGallery()
    }

    private fun loadGallery() {
        viewModelScope.launch {
            delay(1000)
            sendState {
                copy(
                    galleryList = listOf(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWpIU60_Asczj58OQAFyRapji82xX4GJAXoA&s",
                        "2",
                        "3",
                    )
                )
            }
        }
    }
}

data class GalleryUiState(
    val galleryList: List<String> = emptyList()
) : IUiState

sealed class GalleryUiIntent : IUiIntent {
    data object LoadGallery : GalleryUiIntent()
}

class GalleryUiEffect : IUiEffect

val mockGalleryUiState = GalleryUiState(
    galleryList = listOf(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWpIU60_Asczj58OQAFyRapji82xX4GJAXoA&s",
        "2",
        "2",
        "2",
        "3",
    )
)