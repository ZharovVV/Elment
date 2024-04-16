package com.github.elment.core.store

data class ThrottlingConfig(
    val debounceTimeoutMillis: Long = 300L,
    val throttlingWindowDuration: Long = 300L
)
