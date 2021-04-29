package edu.uoc.pac2.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException

/**
 * Awaits for completion of the task without blocking a thread.
 *
 * This suspending function is cancellable.
 * If the [Job] of the current coroutine is cancelled or completed while this suspending function is waiting, this function
 * stops waiting for the completion stage and immediately resumes with [CancellationException].
 */
public suspend fun <T> Task<T>.await(): T? {
    // fast path
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException("Task $this was cancelled normally.")
            } else {
                result
            }
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { continuation ->
        // Add Firebase Task Listener
        addOnCompleteListener {
            // Check Error
            val error = exception
            if (error == null) {
                // Check if Coroutine was cancelled
                if (isCanceled) {
                    continuation.cancel()
                } else{
                    // Task completed correctly
                    continuation.resume(result, onCancellation = null)
                }
            } else {
                // Continue Coroutine by throwing the Firebase Error
                continuation.resumeWithException(error)
            }
        }
    }
}