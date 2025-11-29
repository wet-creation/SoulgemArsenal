package ua.com.xela.soulgem_arsenal.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface PausingScope : CoroutineScope {
    val isPaused: StateFlow<Boolean>

    fun start()
    fun pause()
    fun resume()
    fun stop()

    suspend fun waitIfPaused()
    suspend fun delayAndWaitIfPaused(millis: Long)
}
