package com.github.elment.core.switcher

import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class Switcher {

    private val mutex = Mutex()
    private val jobs: MutableList<Job> = mutableListOf()

    suspend fun switch(action: suspend () -> Unit) {
        coroutineScope {
            val job = launch {
                try {
                    action.invoke()
                } finally {
                    withContext(NonCancellable) {
                        mutex.withLock { jobs -= coroutineContext[Job]!! }
                    }
                }
            }
            mutex.withLock { jobs += job }
        }
    }

    suspend fun await() {

    }

    suspend fun cancel() {

    }
}