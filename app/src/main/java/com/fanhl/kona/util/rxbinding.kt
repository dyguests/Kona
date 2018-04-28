package com.fanhl.kona.util

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable

val <T : View> T.rxClicks: Observable<Any>
    get() {
        return RxView.clicks(this)
    }