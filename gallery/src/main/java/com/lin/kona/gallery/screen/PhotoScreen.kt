package com.lin.kona.gallery.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lin.kona.common.ui.theme.KonaTheme
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val url: String,
)

@Composable
fun PhotoScreen(url: String) {
    KonaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(text = "Photo $url")
            }
        }
    }
}