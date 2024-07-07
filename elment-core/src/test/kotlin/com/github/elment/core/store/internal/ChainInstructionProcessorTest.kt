package com.github.elment.core.store.internal

import com.github.elment.core.TestCommand
import com.github.elment.core.TestEvent
import com.github.elment.core.store.Instruction
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Тест на [ChainInstructionProcessor].
 */
internal class ChainInstructionProcessorTest {

    private val instructionProcessor1: InstructionProcessor<Instruction, TestCommand, TestEvent> =
        mockk {
            every { process(any(), any()) } returns flowOf(
                TestEvent.EventA,
                TestEvent.EventB,
                TestEvent.EventC
            )
        }
    private val instructionProcessor2: InstructionProcessor<Instruction, TestCommand, TestEvent> =
        mockk {
            every { process(any(), any()) } returns flowOf(
                TestEvent.EventD,
                TestEvent.EventE,
                TestEvent.EventF
            )
        }
    private val instructionProcessorsPool: InstructionProcessorsPool<TestCommand, TestEvent> =
        mockk {
            every { get(any<Instruction>()) } returns instructionProcessor1 andThen instructionProcessor2
        }

    private val chainInstructionProcessor = ChainInstructionProcessor<TestCommand, TestEvent>()

    @Test
    fun `verify process`() = runTest {
        val events = chainInstructionProcessor.process(
            instruction = Instruction.ChainExecute(
                instructions = listOf(
                    Instruction.JustExecute(TestCommand.CommandA),
                    Instruction.JustExecute(TestCommand.CommandB)
                )
            ),
            processorsPool = instructionProcessorsPool
        ).toList()

        assertThat(events).isEqualTo(
            listOf(
                TestEvent.EventA,
                TestEvent.EventB,
                TestEvent.EventC,
                TestEvent.EventD,
                TestEvent.EventE,
                TestEvent.EventF
            )
        )
        verifySequence {
            instructionProcessorsPool[Instruction.JustExecute(TestCommand.CommandA)]
            instructionProcessor1.process(
                Instruction.JustExecute(TestCommand.CommandA),
                instructionProcessorsPool
            )
            instructionProcessorsPool[Instruction.JustExecute(TestCommand.CommandB)]
            instructionProcessor2.process(
                Instruction.JustExecute(TestCommand.CommandB),
                instructionProcessorsPool
            )
        }
    }
}