package com.github.elment

import com.github.elment.core.store.dsl.DslReducer
import com.github.elment.core.store.dsl.cancel
import com.github.elment.core.store.dsl.cancellable
import com.github.elment.core.store.dsl.chain
import com.github.elment.core.store.dsl.delay
import com.github.elment.core.store.dsl.schedule
import com.github.elment.models.TestCommand
import com.github.elment.models.TestEffect
import com.github.elment.models.TestEvent
import com.github.elment.models.TestState

internal class TestDslReducer(
    private val uiDslReducer: DslReducer<TestState, TestEffect, TestCommand, TestEvent.Ui>,
    private val internalDslReducer: DslReducer<TestState, TestEffect, TestCommand, TestEvent.Internal>
) : DslReducer<TestState, TestEffect, TestCommand, TestEvent>() {
    override fun Act.reduce(event: TestEvent) {
        when (event) {
            is TestEvent.Ui -> uiDslReducer.delegateReduce(event)
            TestEvent.Internal.OnCurrentLocationLoaded -> {
                state { state }
                effects {
                    +TestEffect.ShowError
                }
                commands {
                    +chain {
                        +cancellable(key = "delayKey", delay(10000))
                        +TestCommand.LoadPerson
                        +cancellable(key = "scoped") {
                            +TestCommand.LoadPerson
                            +TestCommand.LoadCurrentLocation
                        }
                    }
                    +schedule(key = "OnResumeTrigger") {
                        +TestCommand.LoadPerson
                    }
                }
            }

            TestEvent.Internal.OnErrorOccurred -> TODO()
            TestEvent.Internal.OnPersonDataLoaded -> {
                commands {
                    +cancel(key = "delayKey")
                }
            }
        }
    }
}