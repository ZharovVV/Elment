package com.github.elment.sample.ui.case1

data class Case1State(
    val counter: Int,
    val counterText: String
)

sealed interface Case1Event {
    data object Increase : Case1Event
    data object Decrease : Case1Event
}

sealed interface Case1Effect

sealed interface Case1Command
