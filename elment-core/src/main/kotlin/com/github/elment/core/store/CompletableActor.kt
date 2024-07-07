package com.github.elment.core.store

interface CompletableActor<in Command : CompletableCommand> {

    suspend fun execute(command: Command)
}