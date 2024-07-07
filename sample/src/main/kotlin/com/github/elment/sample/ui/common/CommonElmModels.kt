package com.github.elment.sample.ui.common

sealed interface CommonCommand {
    sealed interface TimerCommand : CommonCommand {
        data object Start : TimerCommand
        data object Pause : TimerCommand
        data object Stop : TimerCommand
    }
}

sealed interface CommonEvent {
    data class OnTimeTick(val value: Int) : CommonEvent
}