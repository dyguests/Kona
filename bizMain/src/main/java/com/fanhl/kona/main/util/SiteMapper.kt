package com.fanhl.kona.main.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.ui.graphics.vector.ImageVector
import com.fanhl.kona.main.entity.SiteType

object SiteMapper {
    fun getIcon(siteType: SiteType): ImageVector = when (siteType) {
        SiteType.Danbooru -> Icons.Default.Image
        SiteType.Kona -> Icons.Default.PhotoLibrary
        SiteType.Yandre -> Icons.Default.PictureAsPdf
    }
}