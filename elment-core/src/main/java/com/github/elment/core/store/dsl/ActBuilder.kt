package com.github.elment.core.store.dsl

import com.github.elment.core.store.Act
import com.github.elment.core.store.internal.Operation


open class ActBuilder<State : Any, Effect : Any, Command : Any>(
    initialState: State
) {

    @PublishedApi
    internal var currentState: State = initialState

    @PublishedApi
    internal var effects: List<Effect>? = null

    @PublishedApi
    internal var operation: Operation? = null

    val state: State
        get() = currentState

    inline fun state(action: State.() -> State) {
        currentState = currentState.action()
    }

    inline fun effects(action: EffectsBuilder<Effect>.() -> Unit) {
        effects = EffectsBuilder<Effect>().apply(action).build()
    }

    inline fun commands(action: OperationBuilder<Command>.() -> Unit) {
        operation = OperationBuilder<Command>().apply(action).build()
    }

    internal fun build(): Act<State, Effect> =
        Act(
            state = currentState,
            effects = effects,
            operation = operation
        )
}