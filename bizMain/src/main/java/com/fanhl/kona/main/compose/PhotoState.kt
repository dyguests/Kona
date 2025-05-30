package com.fanhl.kona.main.compose

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@androidx.compose.runtime.Composable
fun rememberPhotoState() = remember { mutableStateOf(PhotoState()) }

data class PhotoState(
    var scale: Float = 1f,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
)

// todo impl