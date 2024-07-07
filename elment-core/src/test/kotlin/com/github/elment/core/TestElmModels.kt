package com.github.elment.core

import com.github.elment.core.store.CompletableCommand

internal object TestState

internal sealed interface TestEvent {
    data object EventA : TestEvent
    data object EventB : TestEvent
    data object EventC : TestEvent
    data object EventD : TestEvent
    data object EventE : TestEvent
    data object EventF : TestEvent
}

internal sealed interface TestEffect {
    data object EffectA : TestEffect
    data object EffectB : TestEffect
}

internal sealed interface TestCommand {
    data object CommandA : TestCommand
    data object CommandB : TestCommand
    data object CommandC : TestCommand
}

internal sealed interface TestCompletableCommand : CompletableCommand {
    data object CommandA : TestCompletableCommand
    data object CommandB : TestCompletableCommand
}