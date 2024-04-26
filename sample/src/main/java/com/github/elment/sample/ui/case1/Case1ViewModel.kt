package com.github.elment.sample.ui.case1

import com.github.elment.android.ElmentViewModel
import com.github.elment.core.config.ThrottlingConfig
import com.github.elment.core.store.DefaultCommandProcessor
import com.github.elment.core.store.DefaultCompletableCommandProcessor
import com.github.elment.core.store.DefaultStore
import com.github.elment.sample.ui.case2.Case2CompletableActor
import com.github.elment.sample.ui.case2.Case2CompletableCommand

internal class Case1ViewModel(

) : ElmentViewModel<Case1State, Case1Event, Case1Effect, Case1Command>(
    store = DefaultStore(
        initialState = Case1State(
            counter = 0,
            counterText = "0"
        ),
        reducer = Case1Reducer(),
        featureCommandProcessor = DefaultCommandProcessor(emptyMap()),
        commonCommandProcessor = DefaultCompletableCommandProcessor(
            mapOf(
                Case2CompletableCommand::class to Case2CompletableActor()
            )
        ),
        throttlingConfig = ThrottlingConfig(
            debounceTimeoutMillis = 1000,
            throttlingWindowDuration = 1000
        )
    )
)