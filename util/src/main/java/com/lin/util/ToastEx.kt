package com.lin.util

import android.widget.Toast

fun toast(text: String) {
    Toast.makeText(ApplicationContext.current, text, Toast.LENGTH_SHORT).show()
}