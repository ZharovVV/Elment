package com.github.elment.sample.models

data class TestState(
    val str: String
)

sealed interface TestEffect {
    data object ShowError : TestEffect
    data object ShowSnackbar : TestEffect
}

sealed interface TestCommand {
    data object LoadPerson : TestCommand
    data object LoadCurrentLocation : TestCommand
}

sealed interface TestEvent {

    sealed interface Ui : TestEvent {
        data object OnResume : Ui
    }

    sealed interface Internal : TestEvent {
        data object OnPersonDataLoaded : Internal
        data object OnCurrentLocationLoaded : Internal
        data object OnErrorOccurred : Internal
    }
}