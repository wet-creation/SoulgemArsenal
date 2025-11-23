package ua.com.xela.soulgem_arsenal.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object MinecraftScope : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext =
        job + Dispatchers.Default + CoroutineName("Soulgem-Client")

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    fun launchInScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        this.launch(context = context, start = start, block = block)
    }

    fun pause() {
        _isPaused.value = true
    }

    fun resume() {
        _isPaused.value = false
    }

    fun stop() {
        job.cancel()
    }

    suspend fun waitIfPaused() {
        isPaused.first { paused -> !paused }
    }
}


object MinecraftServerScope : CoroutineScope {

    private var job: CompletableJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + CoroutineName("Soulgem-Server")

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    fun start() {
        if (job.isCancelled) {
            job = SupervisorJob()
        }
        _isPaused.value = false
    }

    fun launchInScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return this.launch(context = context, start = start, block = block)
    }

    fun pause() {
        _isPaused.value = true
    }

    fun resume() {
        _isPaused.value = false
    }

    fun stop() {
        job.cancel()
    }

    suspend fun waitIfPaused() {
        isPaused.first { paused -> !paused }
    }
}
