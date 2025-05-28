package com.fanhl.kona.main.entity

import androidx.annotation.DrawableRes
import com.fanhl.kona.yandere.R

enum class SiteType(
    val displayName: String,
    @DrawableRes val icon: Int
) {
    Danbooru(
        displayName = "Danbooru",
        icon = com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
    ),
    Konachan(
        displayName = "Konachan",
        icon = com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
    ),
    Yandre(
        displayName = "Yande.re",
        icon = com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
    ),
}