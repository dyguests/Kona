package com.fanhl.util

import androidx.annotation.CheckResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launch a coroutine with error handling support.
 *
 * Use the returned [LaunchTryScope] to specify error handling behavior:
 * - [LaunchTryScope.catch] to handle exceptions with a custom handler.
 * - [LaunchTryScope.forget] to launch the coroutine and ignore exceptions.
 * - [LaunchTryScope.throws] to rethrow exceptions.
 *
 * @return [LaunchTryScope] for specifying error handling behavior.
 */
@CheckResult(suggest = "Use 'catch', 'forget', or 'throws' on the returned LaunchTryScope")
fun CoroutineScope.launchTry(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) = LaunchTryScope(
    context,
    start,
    this,
    block,
)

class LaunchTryScope(
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val start: CoroutineStart = CoroutineStart.DEFAULT,
    private val scope: CoroutineScope,
    private val block: suspend CoroutineScope.() -> Unit,
) {
    private var errorHandler: (Exception) -> Unit = {}

    /**
     * Specify an exception handler to be used if an exception occurs.
     *
     * @param handler The exception handler.
     * @return The [Job] representing the coroutine.
     */
    fun catch(handler: (Exception) -> Unit): Job {
        errorHandler = handler
        return launch(block)
    }

    /**
     * Launch the coroutine and ignore exceptions.
     *
     * @return The [Job] representing the coroutine.
     */
    fun forget(): Job {
        return launch(block)
    }

    /**
     * Launch the coroutine and rethrow exceptions.
     *
     * @return The [Job] representing the coroutine.
     */
    @Throws(Exception::class)
    fun throws(): Job {
        errorHandler = { throw it }
        return launch(block)
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit) = scope.launch(
        context + CoroutineExceptionHandler { _, throwable ->
            catchInternal(throwable)
        },
        start,
    ) {
        block()
    }

    private fun catchInternal(throwable: Throwable) {
        if (throwable is Exception) {
            errorHandler(throwable)
        } else {
            throw throwable
        }
    }
}
