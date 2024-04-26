package com.github.elment.sample.ui.case3

import com.github.elment.core.store.Act
import com.github.elment.core.store.Reducer
import com.github.elment.core.store.internal.Instruction
import com.github.elment.core.store.internal.Operation

internal class Case3Reducer : Reducer<Case3State, Case3Event, Case3Effect> {
    override fun reduce(state: Case3State, event: Case3Event): Act<Case3State, Case3Effect> {
        return when (event) {
            Case3Event.OnStartTimer1ButtonClick -> {
                Act(
                    state = state,
                    effects = null,
                    operation = Operation(
                        instructions = listOf(
                            Instruction.Cancellable.RegisterAndExecute(
                                "timer1",
                                listOf(Instruction.JustExecute(Case3Command.StartTimer1))
                            )
                        )
                    )
                )
            }

            Case3Event.OnCancelTimer1ButtonClick -> {
                Act(
                    state = state,
                    effects = null,
                    operation = Operation(
                        instructions = listOf(
                            Instruction.Cancellable.Cancel("timer1")
                        )
                    )
                )
            }

            Case3Event.CancelTimer2 -> TODO()
            Case3Event.OnExecuteScheduledTimer2ButtonClick -> TODO()
            is Case3Event.OnTimer1Updated -> TODO()
            is Case3Event.OnTimer2Updated -> TODO()
            Case3Event.OnScheduleTimer2ButtonClick -> TODO()
            Case3Event.StartTimer2 -> TODO()
        }
    }
}