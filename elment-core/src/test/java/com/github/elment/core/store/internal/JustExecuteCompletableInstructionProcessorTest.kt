package com.github.elment.core.store.internal

import com.github.elment.core.TestCommand
import com.github.elment.core.TestCompletableCommand
import com.github.elment.core.TestEvent
import com.github.elment.core.store.CompletableCommandProcessor
import com.github.elment.core.store.Instruction
import com.google.common.truth.Truth.assertThat
import io.mockk.coJustRun
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тест на [JustExecuteCompletableInstructionProcessor].
 */
internal class JustExecuteCompletableInstructionProcessorTest {

    private val completableCommandProcessor: CompletableCommandProcessor = mockk {
        coJustRun { process(any()) }
    }

    private val justExecuteCompletableInstructionProcessor =
        JustExecuteCompletableInstructionProcessor<TestCommand, TestEvent>(
            completableCommandProcessor = completableCommandProcessor
        )

    @Test
    fun `verify process`() = runTest {
        val events = justExecuteCompletableInstructionProcessor.process(
            instruction = Instruction.JustExecuteCompletable(TestCompletableCommand.CommandA),
            processorsPool = mockk()
        ).toList()

        assertThat(events).isEmpty()

        coVerifyAll {
            completableCommandProcessor.process(TestCompletableCommand.CommandA)
        }
    }
}