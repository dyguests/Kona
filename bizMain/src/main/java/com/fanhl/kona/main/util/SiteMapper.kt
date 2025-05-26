package com.fanhl.kona.main.util

import com.fanhl.kona.main.entity.SiteType

object SiteMapper {
    fun getIcon(siteType: SiteType): Int = when (siteType) {
        SiteType.Danbooru -> com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
        SiteType.Kona -> com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
        SiteType.Yandre -> com.fanhl.kona.yandere.R.mipmap.ic_favicon_yandere
    }
}