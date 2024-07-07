package com.github.elment.core.store

import kotlin.reflect.KClass

interface Reducer<State : Any, Event : Any, Effect : Any> {

    fun reduce(state: State, event: Event): Act<State, Effect>
}

class ReducerOrchestrator<State : Any, Event : Any, Effect : Any>(
    private val subReducers: Map<KClass<out Event>, Reducer<State, out Event, Effect>>
) : Reducer<State, Event, Effect> {
    @Suppress("UNCHECKED_CAST")
    override fun reduce(state: State, event: Event): Act<State, Effect> {
        val eventKClass = event::class
        val subReducer = (subReducers[eventKClass] as Reducer<State, Event, Effect>?)
            ?: throw NoSuchElementException("Could not find sub reducer for event = $eventKClass")
        return subReducer.reduce(state, event)
    }
}