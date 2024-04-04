package com.github.elment.core.store.dsl

import com.github.elment.core.store.internal.Operation


class OperationBuilder<Command> : InstructionsScope<Command>() {

    @PublishedApi
    internal fun build(): Operation = Operation(instructions)
}