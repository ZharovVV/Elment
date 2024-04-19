package com.github.elment.sample.ui.case3

import com.github.elment.core.store.dsl.DslReducer
import com.github.elment.core.store.dsl.cancel
import com.github.elment.core.store.dsl.cancellable
import com.github.elment.core.store.dsl.chain
import com.github.elment.core.store.dsl.delay
import com.github.elment.sample.ui.case3.Case3Command.StartTimer1

internal class Case3Reducer : DslReducer<Case3State, Case3Effect, Case3Command, Case3Event>() {
    override fun Act.reduce(event: Case3Event) {
        when (event) {
            Case3Event.StartTimer1 -> {
                commands {
                    +cancellable("timer1") {
                        +StartTimer1
                    }
                }
            }

            Case3Event.CancelTimer1 -> {
                commands {
                    +cancel("timer1")
                }
            }

            is Case3Event.OnTimer1Updated -> {
                state {
                    copy(timer1Value = event.timerValue.toString())
                }
            }

            Case3Event.StartTimer2 -> {
                commands {
                    +cancellable("timer2") {
                        +chain {
                            (0..10).forEach {
                                +delay(1000)
                                +Case3Command.UpdateTimer2(it)
                            }
                        }
                    }
                }
            }

            Case3Event.CancelTimer2 -> {
                commands {
                    +cancel("timer2")
                }
            }

            is Case3Event.OnTimer2Updated -> {
                state { copy(timer2Value = event.timerValue.toString()) }
            }
        }
    }

}