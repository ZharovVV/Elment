@file:Suppress("UnusedReceiverParameter")

package com.github.elment.core.store.dsl

import com.github.elment.core.store.CompletableCommand
import com.github.elment.core.store.Instruction
import com.github.elment.core.store.Instruction.Cancellable.Cancel
import com.github.elment.core.store.Instruction.Cancellable.RegisterAndExecute
import com.github.elment.core.store.Instruction.ChainExecute
import com.github.elment.core.store.Instruction.Delay
import com.github.elment.core.store.Instruction.JustExecute
import com.github.elment.core.store.Instruction.JustExecuteCompletable
import com.github.elment.core.store.Instruction.Scheduled.ExecuteScheduled
import com.github.elment.core.store.Instruction.Scheduled.Schedule

open class InstructionsScope<Command> @PublishedApi internal constructor() {

    @PublishedApi
    internal val instructions = mutableListOf<Instruction>()

    operator fun Command.unaryPlus() {
        instructions.add(JustExecute(this))
    }

    operator fun CompletableCommand.unaryPlus() {
        instructions.add(JustExecuteCompletable(this))
    }

    operator fun Instruction.unaryPlus() {
        instructions.add(this)
    }
}


fun <T> InstructionsScope<T>.delay(duration: Long): Instruction = Delay(duration)


inline fun <T> InstructionsScope<T>.chain(action: InstructionsScope<T>.() -> Unit): Instruction {
    val instructions = InstructionsScope<T>().apply(action).instructions
    return ChainExecute(instructions)
}


fun <T> InstructionsScope<T>.cancellable(key: String, instruction: Instruction): Instruction =
    RegisterAndExecute(key, listOf(instruction))


inline fun <T> InstructionsScope<T>.cancellable(
    key: String,
    action: InstructionsScope<T>.() -> Unit
): Instruction {
    val instructions = InstructionsScope<T>().apply(action).instructions
    return RegisterAndExecute(key, instructions)
}


fun <T> InstructionsScope<T>.cancel(key: String): Instruction = Cancel(key)


inline fun <T> InstructionsScope<T>.schedule(
    key: String,
    action: InstructionsScope<T>.() -> Unit
): Instruction {
    val instructions = InstructionsScope<T>().apply(action).instructions
    return Schedule(key, instructions)
}


fun <T> InstructionsScope<T>.executeScheduled(key: String): Instruction = ExecuteScheduled(key)

