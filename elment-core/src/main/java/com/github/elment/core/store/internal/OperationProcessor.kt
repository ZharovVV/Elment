package com.github.elment.core.store.internal

import com.github.elment.core.optin.InternalElmentApi
import com.github.elment.core.store.CommandProcessor
import com.github.elment.core.store.CompletableCommandProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge

internal class OperationProcessor<Command : Any, Event : Any>(
    private val instructionProcessorsPool: InstructionProcessorsPool<Command, Event>
) {

    constructor(
        commandProcessor: CommandProcessor<Command, Event>,
        completableCommandProcessor: CompletableCommandProcessor
    ) : this(InstructionProcessorsPool(commandProcessor, completableCommandProcessor))

    @OptIn(ExperimentalCoroutinesApi::class, InternalElmentApi::class)
    fun process(operation: Operation): Flow<Event> =
        operation.instructions.asFlow()
            .flatMapMerge { instruction ->
                instructionProcessorsPool[instruction]
                    .process(instruction, instructionProcessorsPool)
            }
}