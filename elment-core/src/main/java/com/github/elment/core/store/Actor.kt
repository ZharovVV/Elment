package com.github.elment.core.store

import kotlinx.coroutines.flow.Flow

interface Actor<in Command : Any, out Event : Any> {

    fun execute(command: Command): Flow<Event>
}