package com.github.elment.core.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * TODO
 */
interface Store<State : Any, Event : Any, Effect : Any> {

    val state: StateFlow<State>
    val effects: Flow<Effect>

    fun accept(event: Event, mode: AcceptMode = AcceptMode.DEFAULT)

    fun start()

    fun stop()
}