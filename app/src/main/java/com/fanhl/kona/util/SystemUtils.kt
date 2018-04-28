package com.fanhl.kona.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object SystemUtils {
    fun hideSoftInput(view: View) {
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}