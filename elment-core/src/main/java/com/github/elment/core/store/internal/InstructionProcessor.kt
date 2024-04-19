package com.github.elment.core.store.internal

import com.github.elment.core.store.CommandProcessor
import com.github.elment.core.store.CompletableCommandProcessor
import com.github.elment.core.store.internal.Instruction.Cancellable
import com.github.elment.core.store.internal.Instruction.Cancellable.Cancel
import com.github.elment.core.store.internal.Instruction.Cancellable.RegisterAndExecute
import com.github.elment.core.store.internal.Instruction.ChainExecute
import com.github.elment.core.store.internal.Instruction.Delay
import com.github.elment.core.store.internal.Instruction.JustExecute
import com.github.elment.core.store.internal.Instruction.JustExecuteCompletable
import com.github.elment.core.store.internal.Instruction.Scheduled
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KClass

internal interface InstructionProcessor<InstructionType : Instruction, Command : Any, Event : Any> {

    fun process(
        instruction: InstructionType,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event>
}

internal class JustExecuteInstructionProcessor<Command : Any, Event : Any>(
    private val commandProcessor: CommandProcessor<Command, Event>
) : InstructionProcessor<JustExecute<Command>, Command, Event> {

    override fun process(
        instruction: JustExecute<Command>,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> = commandProcessor.process(instruction.command)
}

internal class JustExecuteCompletableInstructionProcessor<Command : Any, Event : Any>(
    private val completableCommandProcessor: CompletableCommandProcessor
) : InstructionProcessor<JustExecuteCompletable, Command, Event> {
    override fun process(
        instruction: JustExecuteCompletable,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> = flow {
        completableCommandProcessor.process(instruction.command)
    }
}

internal class ChainInstructionProcessor<Command : Any, Event : Any> :
    InstructionProcessor<ChainExecute, Command, Event> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun process(
        instruction: ChainExecute,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> {
        return instruction.instructions.asFlow()
            .flatMapConcat { innerInstruction ->
                val processor = processorsPool[innerInstruction]
                processor.process(innerInstruction, processorsPool)
            }
    }
}

internal class DelayInstructionProcessor<Command : Any, Event : Any> :
    InstructionProcessor<Delay, Command, Event> {

    override fun process(
        instruction: Delay,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> {
        return flow { delay(instruction.duration) }
    }
}

internal class CancellableInstructionProcessor<Command : Any, Event : Any> :
    InstructionProcessor<Cancellable, Command, Event> {

    private val mutex = Mutex()
    private val cancellableJobs: MutableMap<String, Job> = mutableMapOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun process(
        instruction: Cancellable,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> {
        return when (instruction) {
            is Cancel -> flow {
                mutex.withLock { cancellableJobs.remove(instruction.key)?.cancel() }
            }

            is RegisterAndExecute -> {
                channelFlow {
                    val job = launch(start = CoroutineStart.UNDISPATCHED) {
                        instruction.instructions.asFlow()
                            .flatMapMerge { innerInstruction ->
                                processorsPool[innerInstruction]
                                    .process(innerInstruction, processorsPool)
                            }
                            .collect { send(it) }
                    }
                    mutex.withLock {
                        cancellableJobs.remove(instruction.key)?.cancel()
                        cancellableJobs[instruction.key] = job
                    }
                }
            }
        }
    }
}

internal class ScheduledInstructionProcessor<Command : Any, Event : Any> :
    InstructionProcessor<Scheduled, Command, Event> {

    private val mutex = Mutex()
    private val scheduledInstructions: MutableMap<String, Set<Instruction>> = mutableMapOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun process(
        instruction: Scheduled,
        processorsPool: InstructionProcessorsPool<Command, Event>
    ): Flow<Event> {
        return when (instruction) {
            is Scheduled.ExecuteScheduled -> {
                flow {
                    val instructions = mutex.withLock {
                        scheduledInstructions.remove(instruction.key)
                    }
                    if (instructions != null) {
                        emitAll(
                            instructions.asFlow()
                                .flatMapMerge { innerInstruction ->
                                    processorsPool[innerInstruction]
                                        .process(innerInstruction, processorsPool)
                                }
                        )
                    }
                }
            }

            is Scheduled.Schedule -> {
                flow {
                    mutex.withLock {
                        val alreadyScheduledInstructions = scheduledInstructions[instruction.key]
                        scheduledInstructions[instruction.key] =
                            alreadyScheduledInstructions?.plus(instruction.instructions)
                                ?: instruction.instructions.toSet()
                    }
                }
            }
        }
    }
}

internal class InstructionProcessorsPool<Command : Any, Event : Any>(
    commandProcessor: CommandProcessor<Command, Event>,
    completableCommandProcessor: CompletableCommandProcessor
) {
    private val instructionProcessors: Map<KClass<out Instruction>, InstructionProcessor<*, *, *>> =
        mapOf(
            JustExecute::class to JustExecuteInstructionProcessor(commandProcessor),
            JustExecuteCompletable::class to JustExecuteCompletableInstructionProcessor(
                completableCommandProcessor
            ),
            ChainExecute::class to ChainInstructionProcessor(),
            Delay::class to DelayInstructionProcessor(),
            Cancellable::class to CancellableInstructionProcessor(),
            Scheduled::class to ScheduledInstructionProcessor()
        )

    @Suppress("UNCHECKED_CAST")
    operator fun get(instruction: Instruction): InstructionProcessor<Instruction, Command, Event> {
        val instructionClass: KClass<out Instruction> = when (instruction) {
            is Cancellable -> Cancellable::class
            is Scheduled -> Scheduled::class
            else -> instruction::class
        }
        return (instructionProcessors[instructionClass] as InstructionProcessor<Instruction, Command, Event>?)
            ?: throw NoSuchElementException("Could not find processor for instruction = ${instruction::class}")
    }
}

