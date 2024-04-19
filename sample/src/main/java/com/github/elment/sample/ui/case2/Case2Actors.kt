package com.github.elment.sample.ui.case2

import com.github.elment.core.store.CompletableActor
import com.github.elment.core.switcher.Switcher
import kotlinx.coroutines.delay

internal class Case2CompletableActor : CompletableActor<Case2CompletableCommand> {

    private val switcher = Switcher()

    override suspend fun execute(command: Case2CompletableCommand) {
        when (command) {
            Case2CompletableCommand.Command1 -> {
                switcher.switch {
                    delay(300)
                }
            }

            Case2CompletableCommand.Command2 -> TODO()
            Case2CompletableCommand.Command3 -> TODO()
        }
    }
}