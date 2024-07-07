package com.github.elment.sample.ui.case2

internal data class Case2State(
    val counterText: String
)

internal sealed interface Case2Event {
    data object OnStartTimerButtonClick : Case2Event
    data object OnPauseTimerButtonClick : Case2Event
    data object OnStopTimerButtonClick : Case2Event
    data class OnTimeTick(val value: Int) : Case2Event
}

internal sealed interface Case2Command {
    sealed interface TimerCommand : Case2Command {
        data object Start : TimerCommand
        data object Pause : TimerCommand
        data object Stop : TimerCommand
    }
}