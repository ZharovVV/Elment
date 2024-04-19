package com.github.elment.core.config

data class ThrottlingConfig(
    val debounceTimeoutMillis: Long = 300L,
    val throttlingWindowDuration: Long = 300L
) {
    companion object {
        val DEFAULT = ThrottlingConfig()
    }
}
