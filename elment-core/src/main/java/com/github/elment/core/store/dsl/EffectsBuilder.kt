package com.github.elment.core.store.dsl

class EffectsBuilder<Effect> {

    private val effects = mutableListOf<Effect>()

    operator fun Effect.unaryPlus() {
        effects.add(this)
    }

    @PublishedApi
    internal fun build(): List<Effect> = effects
}