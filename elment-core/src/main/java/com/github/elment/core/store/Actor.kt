package com.github.elment.core.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

interface Actor<in Command : Any, out Event : Any> {

    fun execute(command: Command): Flow<Event>
}

typealias Adapter<T, V> = (T) -> V

fun <Command1 : Any, Event1 : Any, Command2 : Any, Event2 : Any> Actor<Command1, Event1>.cast(
    commandAdapter: Adapter<Command2, Command1>,
    eventAdapter: Adapter<Event1, Event2?>
): Actor<Command2, Event2> =
    object : Actor<Command2, Event2> {
        override fun execute(command: Command2): Flow<Event2> =
            execute(commandAdapter.invoke(command)).mapNotNull(eventAdapter)
    }

fun <Command1 : Any, Event1 : Any, Command2 : Any, Event2 : Any> Actor<Command1, Event1>.cast(
    actorAdapter: ActorAdapter<Command1, Event1, Command2, Event2>
): Actor<Command2, Event2> =
    object : Actor<Command2, Event2> {
        override fun execute(command: Command2): Flow<Event2> =
            execute(actorAdapter.adaptCommand(command)).mapNotNull(actorAdapter::adaptEvent)
    }

interface ActorAdapter<Command1 : Any, Event1 : Any, Command2 : Any, Event2 : Any> {
    fun adaptCommand(command: Command2): Command1

    fun adaptEvent(event: Event1): Event2?
}