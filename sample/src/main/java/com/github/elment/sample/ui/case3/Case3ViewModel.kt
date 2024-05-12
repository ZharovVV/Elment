package com.github.elment.sample.ui.case3

import com.github.elment.android.ElmentViewModel
import com.github.elment.core.config.ThrottlingConfig
import com.github.elment.core.store.DefaultCommandProcessor
import com.github.elment.core.store.DefaultCompletableCommandProcessor
import com.github.elment.core.store.DefaultStore

internal class Case3ViewModel : ElmentViewModel<Case3State, Case3Event, Case3Effect, Case3Command>(
    store = DefaultStore(
        initialState = Case3State(
            timer1Value = "",
            timer2Value = ""
        ),
        reducer = Case3DslReducer(),
        featureProcessor = DefaultCommandProcessor(
            mapOf(
                Case3Command.StartTimer1::class to StartTimer1Actor(),
                Case3Command.StartTimer2::class to StartTimer2Actor(),
                Case3Command.UpdateTimer2::class to UpdateTimer2Actor()
            )
        ),
        commonProcessor = DefaultCompletableCommandProcessor(emptyMap()),
        throttlingConfig = ThrottlingConfig(
            debounceTimeout = 500,
            throttlingWindow = 500
        )
    )
)