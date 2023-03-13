package com.lin.kona.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}