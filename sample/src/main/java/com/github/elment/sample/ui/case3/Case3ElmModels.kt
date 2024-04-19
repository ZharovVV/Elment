package com.github.elment.sample.ui.case3

data class Case3State(
    val timer1Value: String,
    val timer2Value: String
)

sealed interface Case3Event {
    data object StartTimer1 : Case3Event
    data object CancelTimer1 : Case3Event

    data class OnTimer1Updated(
        val timerValue: Int
    ) : Case3Event

    data object StartTimer2 : Case3Event
    data object CancelTimer2 : Case3Event

    data class OnTimer2Updated(
        val timerValue: Int
    ) : Case3Event
}

sealed interface Case3Effect

sealed interface Case3Command {
    data object StartTimer1 : Case3Command
    data class UpdateTimer2(val timerValue: Int) : Case3Command
}