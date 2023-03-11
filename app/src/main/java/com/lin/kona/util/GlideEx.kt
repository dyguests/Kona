package com.lin.kona.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

fun ImageView.loadBy(@RawRes @DrawableRes @Nullable resourceId: Int, block: RequestBuilder<Drawable>.() -> Unit) = Glide.with(this)
    .load(resourceId)
    .apply { block() }
    .into(this)