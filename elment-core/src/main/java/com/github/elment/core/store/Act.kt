package com.github.elment.core.store

import com.github.elment.core.store.internal.Operation


data class Act<State : Any, Effect : Any>(
    val state: State,
    val effects: List<Effect>?,
    val operation: Operation?
)