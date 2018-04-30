package com.fanhl.kona.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.fanhl.kona.ui.main.MainActivity

fun <T> LiveData<T>.observe(mainActivity: MainActivity, observer: (T?) -> Unit) = observe(mainActivity, Observer<T> { observer(it) })