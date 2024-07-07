package com.github.elment.core.store

/**
 * Instruction - сущность, которая содержит информацию о том, что и как нужно выполнять.
 * Инструкция может применяться как к командам, так и к другим инструкциям.
 */
sealed interface Instruction {

    /**
     * Инструкция, которая «просто» выполняет переданную [command].
     */
    data class JustExecute<Command>(val command: Command) : Instruction

    /**
     * Инструкция, которая «просто» выполняет переданную [command] ([CompletableCommand]).
     */
    data class JustExecuteCompletable(val command: CompletableCommand) : Instruction

    /**
     * Инструкция, которая выполняет переданные [instructions] "по цепочке",
     * то есть каждая последующая инструкция начнет выполняться только после завершения
     * выполнения предыдущей инструкции.
     */
    data class ChainExecute(val instructions: List<Instruction>) : Instruction

    data class Delay(val duration: Long) : Instruction

    sealed interface Cancellable : Instruction {

        /**
         * Инструкция, которая регистрирует по ключу [key] переданный
         * **отменяемый** набор инструкций [instructions] и после этого параллельно
         * начинает выполнять этот набор инструкций.
         *
         * * Отменяемый набор инструкций с ключом [key] можно отменить вызвав инструкцию [Cancel].
         * В результате отмены выполнение любой инструкции из набора [instructions] полностью прекращается.
         *
         * * Перед регистрацией нового набора отменяемых инструкций выполняется проверка, нет ли в данный момент других отменяемых инструкций с тем же [key].
         * Если такие инструкции уже есть, то они отменяются и регистрируется новый набор инструкций.
         */
        data class RegisterAndExecute(
            val key: String,
            val instructions: List<Instruction>
        ) : Cancellable

        /**
         * Инструкция, которая отменяет набор инструкций,
         * зарегистрированных через [RegisterAndExecute] по ключу [key].
         */
        data class Cancel(val key: String) : Cancellable
    }

    sealed interface Scheduled : Instruction {

        /**
         * Инструкция, которая позволяет запланировать выполнение [instructions].
         *
         * Набор инструкций [instructions] регистрируется по ключу [key] в планировщике
         * и начнет параллельно выполняться только после вызова инструкции [ExecuteScheduled]
         * с соответствующим ключом [key].
         *
         *
         * В случае повторной регистрации [instructions],
         * если в планировщике для ключа [key] уже существует набор инструкций,
         * то проверяется каждая инструкция в наборе:
         * * Если инструкция уникальна и не равна (!equals)
         * ни одной уже существующей инструкции в планировщике -
         * то она будет добавлена в уже существующий набор инструкций
         * * Если инструкция неуникальна -
         * она не будет добавлена в уже существующий набор инструкций.
         */
        data class Schedule(
            val key: String,
            val instructions: List<Instruction>
        ) : Scheduled

        /**
         * Инструкция для выполнения набора запланированных инструкций с ключом [key].
         *
         * Запланированный набор инструкций будет выполняться параллельно.
         *
         * Инструкции, которые начали выполняться, автоматически удаляются из планировщика.
         */
        data class ExecuteScheduled(val key: String) : Scheduled
    }
}