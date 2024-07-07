package com.github.elment.core.config

import com.github.elment.core.store.config.ThrottlingConfig
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Тест на [ThrottlingConfig].
 */
internal class ThrottlingConfigKtTest {

    @Test
    fun `verify ThrottlingConfig Default`() {
        val defaultThrottlingConfig = ThrottlingConfig.Default

        assertThat(defaultThrottlingConfig.debounceTimeout.invoke(Any())).isEqualTo(300L)
        assertThat(defaultThrottlingConfig.throttlingWindow.invoke(Any())).isEqualTo(300L)
    }

    @Test
    fun `verify ThrottlingConfig`() {
        val debounceTimeout = 100500L
        val throttlingWindow = 1005001L

        val throttlingConfig = ThrottlingConfig(debounceTimeout, throttlingWindow)

        assertThat(throttlingConfig.debounceTimeout.invoke(Any())).isEqualTo(debounceTimeout)
        assertThat(throttlingConfig.throttlingWindow.invoke(Any())).isEqualTo(throttlingWindow)
    }
}