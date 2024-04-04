package com.github.elment.core.store

interface Reducer<State : Any, Effect : Any, Event : Any> {

    fun reduce(state: State, event: Event): Act<State, Effect>
}