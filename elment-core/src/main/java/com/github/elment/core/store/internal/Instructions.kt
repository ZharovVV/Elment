package com.github.elment.core.store.internal

import com.github.elment.core.store.CompletableCommand

sealed interface Instruction {

    data class JustExecute<Command>(val command: Command) : Instruction

    data class JustExecuteCompletable(val command: CompletableCommand) : Instruction

    data class ChainExecute(
        val instructions: List<Instruction>
    ) : Instruction

    data class Delay(val duration: Long) : Instruction

    sealed interface Cancellable : Instruction {

        data class RegisterAndExecute(
            val key: String,
            val instructions: List<Instruction>
        ) : Cancellable

        data class Cancel(val key: String) : Cancellable
    }

    sealed interface Scheduled : Instruction {

        data class Schedule(
            val key: String,
            val instructions: List<Instruction>
        ) : Scheduled

        data class ExecuteScheduled(val key: String) : Scheduled
    }
}