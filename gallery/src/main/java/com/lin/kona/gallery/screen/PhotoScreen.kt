package com.lin.kona.gallery.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val url: String,
)

@Composable
fun PhotoScreen() {
    Text(text = "Photo Screen")
}