package com.lin.kona.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

fun ImageView.loadBy(
    string: String,
    block: RequestBuilder<Drawable>.() -> Unit
) = Glide.with(this)
    .load(string)
    .apply { block() }
    .into(this)

fun ImageView.loadBy(
    @RawRes @DrawableRes resourceId: Int,
    block: RequestBuilder<Drawable>.() -> Unit
) = Glide.with(this)
    .load(resourceId)
    .apply { block() }
    .into(this)