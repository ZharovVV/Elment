package com.github.elment.android

import androidx.lifecycle.ViewModel
import com.github.elment.core.store.AcceptMode
import com.github.elment.core.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

open class ElmentViewModel<State : Any, Effect : Any, Command : Any, Event : Any>(
    private val store: Store<State, Effect, Event>
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

fun <State : Any, Effect : Any, Command : Any, Event : Any> ElmentViewModel<State, Effect, Command, Event>.acceptDebounce(
    event: Event
) {
    accept(event, mode = AcceptMode.DEBOUNCE)
}

fun <State : Any, Effect : Any, Command : Any, Event : Any> ElmentViewModel<State, Effect, Command, Event>.acceptDropOldest(
    event: Event
) {
    accept(event, mode = AcceptMode.DROP_OLDEST)
}

fun <State : Any, Effect : Any, Command : Any, Event : Any> ElmentViewModel<State, Effect, Command, Event>.acceptThrottle(
    event: Event
) {
    accept(event, mode = AcceptMode.THROTTLE)
}