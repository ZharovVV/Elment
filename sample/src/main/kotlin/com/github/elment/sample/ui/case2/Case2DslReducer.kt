package com.github.elment.sample.ui.case2

import com.github.elment.core.store.dsl.DslReducer

internal class Case2DslReducer : DslReducer<Case2State, Case2Event, Nothing, Case2Command>() {
    override fun Act.reduce(event: Case2Event) {
        when (event) {
            Case2Event.OnPauseTimerButtonClick -> {
                commands { +Case2Command.TimerCommand.Pause }
            }

            Case2Event.OnStartTimerButtonClick -> {
                commands { +Case2Command.TimerCommand.Start }
            }

            Case2Event.OnStopTimerButtonClick -> {
                commands { +Case2Command.TimerCommand.Stop }
            }

            is Case2Event.OnTimeTick -> {
                state {
                    copy(counterText = event.value.toString())
                }
            }
        }
    }
}