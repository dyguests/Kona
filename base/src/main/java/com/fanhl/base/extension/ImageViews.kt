package com.fanhl.base.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.setImage(
    url: String,
    @DrawableRes placeHolder: Int? = null
) {
    Glide.with(this)
        .load(url)
        .into(this)
}
