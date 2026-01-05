package com.business.cmpproject.core.network

import kotlinx.coroutines.delay

suspend fun <T> retry(
    times: Int = 2,
    delayMillis: Long = 800,
    block: suspend () -> T
): T {
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            if (attempt == times - 1) throw e
            delay(delayMillis)
        }
    }
    throw IllegalStateException("Retry failed")
}