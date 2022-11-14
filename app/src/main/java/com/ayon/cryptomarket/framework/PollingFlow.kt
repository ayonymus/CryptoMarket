package com.ayon.cryptomarket.framework

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

/**
 * Creates a cold flow that executes the same task repeatedly.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> pollingFlowOf(
    delayMS: Long = 5000,
    maxRepeat: Int = 100,
    task: suspend () -> T): Flow<T> {
    return channelFlow {
        var currentRepeat = 0
        while (!isClosedForSend && currentRepeat < maxRepeat) {
            currentRepeat += 1
            send(task())
            delay(delayMS)
        }
    }
}
