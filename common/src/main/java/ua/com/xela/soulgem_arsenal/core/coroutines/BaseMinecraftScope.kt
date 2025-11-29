package ua.com.xela.soulgem_arsenal.core.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import net.minecraft.util.thread.ReentrantBlockableEventLoop
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseMinecraftScope(
    private val scopeName: String
) : PausingScope {

    private var job: CompletableJob = SupervisorJob()
    private var dispatcher: CoroutineDispatcher? = null

    // Expose paused state
    private val _isPaused = MutableStateFlow(false)
    override val isPaused: StateFlow<Boolean> get() = _isPaused

    // Attach dispatcher from outside (client/server)
    protected fun setDispatcher(dispatcher: CoroutineDispatcher) {
        this.dispatcher = dispatcher
    }

    override val coroutineContext: CoroutineContext
        get() = job +
                (dispatcher ?: error("Dispatcher is not attached for $scopeName")) +
                CoroutineName(scopeName)

    override fun start() {
        if (job.isCancelled) {
            job = SupervisorJob()
        }
        _isPaused.value = false
    }

    override fun pause() {
        _isPaused.value = true
    }

    override fun resume() {
        _isPaused.value = false
    }

    override fun stop() {
        job.cancel()
    }

    override suspend fun waitIfPaused() {
        isPaused.first { paused -> !paused }
    }

    override suspend fun delayAndWaitIfPaused(millis: Long) {
        waitIfPaused()
        delay(millis)
    }

    // Unified launch helper
    fun launchInScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend BaseMinecraftScope.() -> Unit
    ): Job = launch(context = context, start = start) {
        block()
    }
}


fun ReentrantBlockableEventLoop<*>.asCoroutineDispatcher(): CoroutineDispatcher =
    object : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            this@asCoroutineDispatcher.execute(block)
        }
    }