package com.github.elment.sample.ui.common

import com.github.elment.core.optin.ExperimentalElmentApi
import com.github.elment.core.store.Actor
import com.github.elment.core.switcher.Switcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalElmentApi::class)
internal class TimerActor : Actor<CommonCommand.TimerCommand, CommonEvent> {

    private val switcher = Switcher()

    //not thread safe as this is just an example
    private var timerValue: Int = 0

    override fun execute(command: CommonCommand.TimerCommand): Flow<CommonEvent> {
        return when (command) {
            CommonCommand.TimerCommand.Start -> switcher.switchFlow {
                generateSequence(seed = timerValue) { it + 1 }
                    .asFlow()
                    .onEach {
                        delay(1000)
                        timerValue = it
                    }
                    .map { CommonEvent.OnTimeTick(it) }
            }

            CommonCommand.TimerCommand.Pause -> flow {
                switcher.cancel()
            }

            CommonCommand.TimerCommand.Stop -> flow {
                switcher.cancel()
                timerValue = 0
            }
        }
    }

}