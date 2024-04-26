package com.github.elment.core.store

import com.github.elment.core.config.ThrottlingConfig
import com.github.elment.core.store.internal.Operation
import com.github.elment.core.store.internal.OperationProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

typealias StateMapper<InternalState, UiState> = (internalState: InternalState) -> UiState


internal class StoreImpl<UiState : Any, InternalState : Any, Event : Any, Effect : Any, Command : Any>(
    private val initialState: InternalState,
    private val reducer: Reducer<InternalState, Event, Effect>,
    private val operationProcessor: OperationProcessor<Command, Event>,
    stateMapper: StateMapper<InternalState, UiState>,
    throttlingConfig: ThrottlingConfig
) : Store<UiState, Event, Effect> {

    private val storeScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    //region State
    private val _state: MutableSharedFlow<InternalState> = MutableSharedFlow<InternalState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply { tryEmit(initialState) }

    override val state: StateFlow<UiState> = _state
        .map(stateMapper)
        .stateIn(storeScope, SharingStarted.Eagerly, stateMapper.invoke(initialState))
    //endregion

    //region Effect
    private val effectsChannel: Channel<Effect> = Channel(Channel.BUFFERED)
    override val effects: Flow<Effect> = effectsChannel.receiveAsFlow()
    //endregion

    private val defaultUiEvents = MutableSharedFlow<Event>()
    private val debounceUiEvents = MutableSharedFlow<Event>()
    private val throttledUiEvents = MutableSharedFlow<Event>()
    private val dropOldestUiEvents = MutableSharedFlow<Event>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val internalEvents = MutableSharedFlow<Event>()

    @OptIn(FlowPreview::class)
    private val events: Flow<Event> = merge(
        defaultUiEvents,
        debounceUiEvents.debounce(throttlingConfig.debounceTimeoutMillis),
        throttledUiEvents.throttleFirst(throttlingConfig.throttlingWindowDuration),
        dropOldestUiEvents,
        internalEvents
    )

    override fun accept(event: Event, mode: AcceptMode) {
        when (mode) {
            AcceptMode.DEFAULT -> storeScope.launch { defaultUiEvents.emit(event) }
            AcceptMode.DROP_OLDEST -> dropOldestUiEvents.tryEmit(event)
            AcceptMode.DEBOUNCE -> storeScope.launch { debounceUiEvents.emit(event) }
            AcceptMode.THROTTLE -> storeScope.launch { throttledUiEvents.emit(event) }
        }
    }

    override fun start() {
        _state.zip(events, reducer::reduce)
            .onEach { act: Act<InternalState, Effect> ->
                _state.emit(act.state)
                act.effects?.forEach { effectsChannel.send(it) }
                act.operation?.let(::executeOperation)
            }
            .launchIn(storeScope)
        runBlocking { defaultUiEvents.subscriptionCount.first { it > 0 } }
    }

    override fun stop() {
        storeScope.coroutineContext.cancelChildren()
    }

    private fun executeOperation(operation: Operation) {
        operationProcessor.process(operation)
            .onEach(internalEvents::emit)
            .launchIn(storeScope)
    }
}

private fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastTime = 0L
    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime >= windowDuration) {
            lastTime = currentTime
            emit(value)
        }
    }
}

@Suppress("FunctionName")
fun <State : Any, Event : Any, Effect : Any, Command : Any> DefaultStore(
    initialState: State,
    reducer: Reducer<State, Event, Effect>,
    featureCommandProcessor: CommandProcessor<Command, Event>,
    commonCommandProcessor: CompletableCommandProcessor,
    throttlingConfig: ThrottlingConfig = ThrottlingConfig.DEFAULT
): Store<State, Event, Effect> =
    StoreImpl(
        initialState = initialState,
        reducer = reducer,
        operationProcessor = OperationProcessor(featureCommandProcessor, commonCommandProcessor),
        stateMapper = { it },
        throttlingConfig
    )

@Suppress("FunctionName")
fun <UiState : Any, InternalState : Any, Event : Any, Effect : Any, Command : Any> StoreWithStateMapper(
    initialState: InternalState,
    reducer: Reducer<InternalState, Event, Effect>,
    featureCommandProcessor: CommandProcessor<Command, Event>,
    commonCommandProcessor: CompletableCommandProcessor,
    stateMapper: StateMapper<InternalState, UiState>,
    throttlingConfig: ThrottlingConfig = ThrottlingConfig.DEFAULT
): Store<UiState, Event, Effect> =
    StoreImpl(
        initialState = initialState,
        reducer = reducer,
        operationProcessor = OperationProcessor(featureCommandProcessor, commonCommandProcessor),
        stateMapper = stateMapper,
        throttlingConfig
    )



