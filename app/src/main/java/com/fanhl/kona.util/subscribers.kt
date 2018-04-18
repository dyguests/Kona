package com.fanhl.kona.util

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.RuntimeException

/**
 * see io/reactivex/rxkotlin/subscribers.kt
 */

private val onSubscribeStub: (d: Disposable) -> Unit = {}
private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = { throw OnErrorNotImplementedException(it) }
private val onCompleteStub: () -> Unit = {}

/**
 * Overloaded subscribe function that allow passing named parameters
 *
 * rxJava1
 */
fun <T : Any> Observable<T>.subscribeBy(
        onSubscribe: (d: Disposable) -> Unit = onSubscribeStub,
        onNext: (T) -> Unit = onNextStub,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
): Unit = subscribe(object : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        onSubscribe(d)
    }

    override fun onNext(t: T) {
        onNext(t)
    }

    override fun onError(e: Throwable) {
        onError(e)
    }

    override fun onComplete() {
        onComplete()
    }
})

class OnErrorNotImplementedException(e: Throwable) : RuntimeException(e)
