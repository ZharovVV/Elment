package com.github.elment.sample.ui.case2

import com.github.elment.core.store.CompletableCommand

sealed interface Case2CompletableCommand : CompletableCommand {
    data object Command1 : Case2CompletableCommand
    data object Command2 : Case2CompletableCommand
    data object Command3 : Case2CompletableCommand
}