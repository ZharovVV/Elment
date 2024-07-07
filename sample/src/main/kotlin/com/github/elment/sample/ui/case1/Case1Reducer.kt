package com.github.elment.sample.ui.case1

import com.github.elment.core.store.dsl.DslReducer

internal class Case1Reducer : DslReducer<Case1State, Case1Event, Case1Effect, Case1Command>() {

    override fun Act.reduce(event: Case1Event) {
        when (event) {
            Case1Event.Decrease -> {
                state {
                    val newValue = counter - 1
                    copy(
                        counter = newValue,
                        counterText = newValue.toString()
                    )
                }
            }

            Case1Event.Increase -> {
                state {
                    val newValue = counter + 1
                    copy(
                        counter = newValue,
                        counterText = newValue.toString()
                    )
                }
            }
        }
    }
}