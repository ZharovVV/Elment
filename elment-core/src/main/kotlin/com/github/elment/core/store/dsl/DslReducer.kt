package com.github.elment.core.store.dsl

import com.github.elment.core.store.Reducer

abstract class DslReducer<State : Any, Event : Any, Effect : Any, Command : Any> :
    Reducer<State, Event, Effect> {

    final override fun reduce(
        state: State,
        event: Event
    ): com.github.elment.core.store.Act<State, Effect> {
        return with(Act(state)) {
            reduce(event)
            build()
        }
    }

    //Для уменьшения количества кода
    protected inner class Act(state: State) : ActBuilder<State, Effect, Command>(state) {

        fun <DelegateEvent : Event> DslReducer<State, DelegateEvent, Effect, Command>.delegateReduce(
            event: DelegateEvent
        ) {
            val act = reduce(currentState, event)
            copy(act)
        }

        private fun copy(act: com.github.elment.core.store.Act<State, Effect>) {
            currentState = act.state
            effects = act.effects
            operation = act.operation
        }
    }

    protected abstract fun Act.reduce(event: Event)
}