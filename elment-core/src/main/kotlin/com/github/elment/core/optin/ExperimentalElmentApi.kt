package com.github.elment.core.optin

@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is an experimental com.github.elment API, " +
            "which means that the design of the corresponding declarations has open issues which may (or may not) lead to their changes in the future."
)
annotation class ExperimentalElmentApi
