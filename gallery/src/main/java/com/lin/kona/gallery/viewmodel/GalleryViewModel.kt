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
                        "https://q1.itc.cn/q_70/images03/20240329/23a82de4541a454f85d81e71074a7141.jpeg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSndAm8rGbVetP_9R2YzFhvlw1-qmHY5bUGCg&s",
                        "https://q1.itc.cn/q_70/images03/20240329/23a82de4541a454f85d81e71074a7141.jpeg",
                        "https://q1.itc.cn/q_70/images03/20240329/23a82de4541a454f85d81e71074a7141.jpeg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSndAm8rGbVetP_9R2YzFhvlw1-qmHY5bUGCg&s",
                        "https://q1.itc.cn/q_70/images03/20240329/23a82de4541a454f85d81e71074a7141.jpeg",
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
        "https://q1.itc.cn/q_70/images03/20240329/23a82de4541a454f85d81e71074a7141.jpeg",
        "2",
        "2",
        "2",
        "3",
    )
)