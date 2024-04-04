@file:Suppress("UnusedReceiverParameter")

package com.github.elment.core.store.dsl

import com.github.elment.core.store.CompletableCommand
import com.github.elment.core.store.internal.Instruction
import com.github.elment.core.store.internal.Instruction.Cancellable.Cancel
import com.github.elment.core.store.internal.Instruction.Cancellable.RegisterAndExecute
import com.github.elment.core.store.internal.Instruction.ChainExecute
import com.github.elment.core.store.internal.Instruction.Delay
import com.github.elment.core.store.internal.Instruction.JustExecute
import com.github.elment.core.store.internal.Instruction.JustExecuteCompletable
import com.github.elment.core.store.internal.Instruction.Scheduled.ExecuteScheduled
import com.github.elment.core.store.internal.Instruction.Scheduled.Schedule


open class InstructionsScope<Command> {

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

