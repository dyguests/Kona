package com.fanhl.kona.main.entity

import androidx.annotation.DrawableRes

enum class SiteType(
    val displayName: String,
    @DrawableRes val icon: Int
) {
    Danbooru(
        displayName = "Danbooru",
        icon = com.fanhl.kona.danbooru.R.mipmap.ic_favicon_danbooru
    ),
    Konachan(
        displayName = "Konachan",
        icon = com.fanhl.kona.kona.R.mipmap.ic_favicon_konachan
    ),
    Yandre(
        displayName = "Yande.re",
        icon = com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
    ),
}