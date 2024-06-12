package com.github.elment.sample.ui.case2

import com.github.elment.android.ElmentViewModel
import com.github.elment.core.store.ActorAdapter
import com.github.elment.core.store.DefaultCommandProcessor
import com.github.elment.core.store.DefaultStore
import com.github.elment.core.store.cast
import com.github.elment.sample.ui.common.CommonCommand
import com.github.elment.sample.ui.common.CommonEvent
import com.github.elment.sample.ui.common.TimerActor

internal class Case2ViewModel : ElmentViewModel<Case2State, Case2Event, Nothing, Case2Command>(
    store = DefaultStore(
        initialState = Case2State(counterText = ""),
        reducer = Case2DslReducer(),
        commandProcessor = DefaultCommandProcessor(
            actors = buildMap {
                val timerActor = TimerActor().cast(Case2TimerActorAdapter)
                set(Case2Command.TimerCommand.Start::class, timerActor)
                set(Case2Command.TimerCommand.Stop::class, timerActor)
                set(Case2Command.TimerCommand.Pause::class, timerActor)
            }
        )
    )
)

internal object Case2TimerActorAdapter :
    ActorAdapter<CommonCommand.TimerCommand, CommonEvent, Case2Command.TimerCommand, Case2Event.OnTimeTick> {
    override fun adaptCommand(
        command: Case2Command.TimerCommand
    ): CommonCommand.TimerCommand = when (command) {
        Case2Command.TimerCommand.Pause -> CommonCommand.TimerCommand.Pause
        Case2Command.TimerCommand.Start -> CommonCommand.TimerCommand.Start
        Case2Command.TimerCommand.Stop -> CommonCommand.TimerCommand.Stop
    }

    override fun adaptEvent(event: CommonEvent): Case2Event.OnTimeTick? =
        when (event) {
            is CommonEvent.OnTimeTick -> Case2Event.OnTimeTick(event.value)
            else -> null
        }

}