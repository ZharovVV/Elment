package com.github.elment.sample.ui.case3

import com.github.elment.core.store.Actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class StartTimer1Actor : Actor<Case3Command.StartTimer1, Case3Event> {
    override fun execute(command: Case3Command.StartTimer1): Flow<Case3Event> =
        (0..10).asFlow()
            .onEach { delay(1000) }
            .map(Case3Event::OnTimer1Updated)

}

internal class StartTimer2Actor : Actor<Case3Command.StartTimer2, Case3Event> {
    override fun execute(command: Case3Command.StartTimer2): Flow<Case3Event> =
        (0..10).asFlow()
            .onEach { delay(1000) }
            .map(Case3Event::OnTimer2Updated)

}

internal class UpdateTimer2Actor : Actor<Case3Command.UpdateTimer2, Case3Event> {
    override fun execute(command: Case3Command.UpdateTimer2): Flow<Case3Event> =
        flowOf(Case3Event.OnTimer2Updated(command.timerValue))
}