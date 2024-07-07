package com.github.elment.core.optin

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is an internal com.github.elment API that " +
            "should not be used from outside of com.github.elment.\n" +
            "Can only be used in Unit tests."
)
annotation class InternalElmentApi
