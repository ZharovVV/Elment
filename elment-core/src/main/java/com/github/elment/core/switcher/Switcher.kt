package com.github.elment.core.switcher

import com.github.elment.core.optin.ExperimentalElmentApi
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

@ExperimentalElmentApi
class Switcher {

    private val previousJob: AtomicRef<Job?> = atomic(null)

    suspend fun switch(action: suspend () -> Unit) {
        switchInternal(action)
    }

    fun <Event : Any> switchFlow(
        action: suspend () -> Flow<Event>
    ): Flow<Event> = channelFlow {
        switchInternal {
            action().collect { send(it) }
        }
    }

    private suspend fun switchInternal(action: suspend () -> Unit) {
        coroutineScope {
            val newJob = launch(start = CoroutineStart.UNDISPATCHED) {
                try {
                    action.invoke()
                } finally {
                    //prevent job leakage
                    previousJob.compareAndSet(coroutineContext[Job], null)
                }
            }
            previousJob.getAndSet(newJob)?.cancelAndJoin()
        }
    }

    suspend fun await() {
        previousJob.value?.join()
    }

    suspend fun cancel() {
        previousJob.value?.cancelAndJoin()
    }
}