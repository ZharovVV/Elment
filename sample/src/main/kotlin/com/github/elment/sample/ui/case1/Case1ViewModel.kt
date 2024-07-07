package com.github.elment.sample.ui.case1

import com.github.elment.android.ElmentViewModel
import com.github.elment.core.store.DefaultCommandProcessor
import com.github.elment.core.store.DefaultCompletableCommandProcessor
import com.github.elment.core.store.DefaultStore

internal class Case1ViewModel(

) : ElmentViewModel<Case1State, Case1Event, Case1Effect, Case1Command>(
    store = DefaultStore(
        initialState = Case1State(
            counter = 0,
            counterText = "0"
        ),
        reducer = Case1Reducer(),
        commandProcessor = DefaultCommandProcessor(emptyMap()),
        completableCommandProcessor = DefaultCompletableCommandProcessor(emptyMap()),
        throttlingConfig = Case1ThrottlingConfig()
    )
)