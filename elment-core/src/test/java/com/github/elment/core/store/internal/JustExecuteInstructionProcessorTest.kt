package com.github.elment.core.store.internal

import com.github.elment.core.TestCommand
import com.github.elment.core.TestEvent
import com.github.elment.core.store.CommandProcessor
import com.github.elment.core.store.Instruction
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тест на [JustExecuteInstructionProcessor].
 */
internal class JustExecuteInstructionProcessorTest {

    private val commandProcessor: CommandProcessor<TestCommand, TestEvent> = mockk {
        every { process(any()) } returns flowOf(TestEvent.EventA, TestEvent.EventB)
    }

    private val justExecuteInstructionProcessor = JustExecuteInstructionProcessor(
        commandProcessor = commandProcessor
    )

    @Test
    fun `verify process`() = runTest {
        val events = justExecuteInstructionProcessor.process(
            instruction = Instruction.JustExecute(TestCommand.CommandA),
            processorsPool = mockk()
        ).toList()

        assertThat(events).isEqualTo(
            listOf(
                TestEvent.EventA,
                TestEvent.EventB
            )
        )

        verifyAll {
            commandProcessor.process(TestCommand.CommandA)
        }
    }
}

