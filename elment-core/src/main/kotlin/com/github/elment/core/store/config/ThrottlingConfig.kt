package com.github.elment.core.store.config

typealias TimeSelector<T> = (T) -> Long

interface ThrottlingConfig<in Event> {

    val debounceTimeout: TimeSelector<Event>
    val throttlingWindow: TimeSelector<Event>

    companion object Default : ThrottlingConfig<Any> {
        override val debounceTimeout: TimeSelector<Any> = { 300 }
        override val throttlingWindow: TimeSelector<Any> = { 300 }
    }
}

fun ThrottlingConfig(
    debounceTimeout: Long,
    throttlingWindow: Long
) = object : ThrottlingConfig<Any> {
    override val debounceTimeout: TimeSelector<Any> = { debounceTimeout }
    override val throttlingWindow: TimeSelector<Any> = { throttlingWindow }
}


