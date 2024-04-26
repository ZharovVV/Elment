package com.github.elment.core.store

interface Reducer<State : Any, Event : Any, Effect : Any> {

    fun reduce(state: State, event: Event): Act<State, Effect>
}