package com.fanhl.util

import android.content.res.Resources

/** px2dp */
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
/** dp2px */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()