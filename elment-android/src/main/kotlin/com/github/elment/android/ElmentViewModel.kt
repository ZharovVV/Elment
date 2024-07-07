package com.github.elment.android

import androidx.lifecycle.ViewModel
import com.github.elment.core.store.AcceptMode
import com.github.elment.core.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

open class ElmentViewModel<State : Any, Event : Any, Effect : Any, Command : Any>(
    private val store: Store<State, Event, Effect>
) : ViewModel(Closeable { store.stop() }) {

    val state: StateFlow<State> get() = store.state
    val effects: Flow<Effect> get() = store.effects

    init {
        store.start()
    }

    fun accept(event: Event, mode: AcceptMode = AcceptMode.DEFAULT) {
        store.accept(event, mode)
    }
}

fun <State : Any, Event : Any, Effect : Any, Command : Any> ElmentViewModel<State, Event, Effect, Command>.acceptDebounce(
    event: Event
) {
    accept(event, mode = AcceptMode.DEBOUNCE)
}

fun <State : Any, Event : Any, Effect : Any, Command : Any> ElmentViewModel<State, Event, Effect, Command>.acceptDropOldest(
    event: Event
) {
    accept(event, mode = AcceptMode.DROP_OLDEST)
}

fun <State : Any, Event : Any, Effect : Any, Command : Any> ElmentViewModel<State, Event, Effect, Command>.acceptThrottle(
    event: Event
) {
    accept(event, mode = AcceptMode.THROTTLE)
}