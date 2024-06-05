package com.github.elment.core.store

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface CommandProcessor<Command : Any, Event : Any> {
    fun process(command: Command): Flow<Event>
}

class DefaultCommandProcessor<Command : Any, Event : Any>(
    private val actors: Map<KClass<out Command>, Actor<*, *>>
) : CommandProcessor<Command, Event> {

    @Suppress("UNCHECKED_CAST")
    override fun process(command: Command): Flow<Event> {
        val commandKClass = command::class
        val actor = (actors[commandKClass] as Actor<Command, Event>?)
            ?: throw NoSuchElementException("Could not find actors for command = $commandKClass")
        return actor.execute(command)
    }
}

interface CompletableCommandProcessor {
    suspend fun process(command: CompletableCommand)

    companion object Empty : CompletableCommandProcessor {
        override suspend fun process(command: CompletableCommand) {
            //do nothing
        }
    }
}

class DefaultCompletableCommandProcessor(
    private val completableActors: Map<KClass<out CompletableCommand>, CompletableActor<*>>
) : CompletableCommandProcessor {

    @Suppress("UNCHECKED_CAST")
    override suspend fun process(command: CompletableCommand) {
        val commandKClass = command::class
        val actor = (completableActors[commandKClass] as CompletableActor<CompletableCommand>?)
            ?: throw NoSuchElementException("Could not find actors for command = $commandKClass")
        actor.execute(command)
    }
}
