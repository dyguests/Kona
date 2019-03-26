package com.fanhl.base.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImage(
    url: String,
    @DrawableRes placeHolder: Int? = null
) {
    Glide.with(this)
        .load(url)
        .apply {
            if (placeHolder != null) {
                apply(RequestOptions().placeholder(placeHolder))
            }
        }
        .into(this)
}
