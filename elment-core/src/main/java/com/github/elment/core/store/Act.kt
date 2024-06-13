package com.github.elment.core.store


data class Act<State : Any, Effect : Any>(
    val state: State,
    val effects: List<Effect>?,
    val operation: Operation?
)