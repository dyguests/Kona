package com.lin.kona.net.util

import retrofit2.Response
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
public fun <T> Response<T>.whenSuccess(block: Response<T>. () -> Unit): Response<T> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    if (this.isSuccessful) {
        block(this)
    }
    return this
}
