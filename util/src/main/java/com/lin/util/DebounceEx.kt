package com.lin.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 防抖间隔
 */
private var DEBOUNCE_INTERVAL = 1000L

@Composable
fun rememberDebouncer(): Debouncer {
    return rememberSaveable(saver = DebouncerImpl.Saver) {
        DebouncerImpl()
    }
}

interface Debouncer {
    fun invoke(block: () -> Unit)

    companion object {
        fun create(): Debouncer {
            return DebouncerImpl()
        }
    }
}

internal class DebouncerImpl : Debouncer {
    private var lastTime = 0L

    override fun invoke(block: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > DEBOUNCE_INTERVAL) {
            lastTime = currentTime
            block()
        }
    }

    companion object {
        val Saver: Saver<DebouncerImpl, *> = Saver(
            save = { it.lastTime },
            restore = { DebouncerImpl().apply { lastTime = it } }
        )
    }
}

fun View.debounceClick(block: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastTime = 0L
        override fun onClick(v: View) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime > DEBOUNCE_INTERVAL) {
                lastTime = currentTime
                block()
            }
        }
    })
}
