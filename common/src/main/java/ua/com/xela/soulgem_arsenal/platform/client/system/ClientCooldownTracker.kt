package ua.com.xela.soulgem_arsenal.platform.client.system

import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownTickPayload
import ua.com.xela.soulgem_arsenal.skills.base.CooldownKey

object ClientCooldownTracker {

    data class CooldownState(
        val totalSeconds: Int,
        val remainingSeconds: Int
    ) {
        val progress: Float
            get() = if (totalSeconds <= 0) 0f
            else 1f - (remainingSeconds.coerceIn(0, totalSeconds).toFloat() / totalSeconds.toFloat())
    }

    private val states = mutableMapOf<CooldownKey, CooldownState>()

    fun onStart(payload: CooldownStartPayload) {
        states[payload.key.toCooldownKey()] = CooldownState(
            totalSeconds = payload.totalSeconds,
            remainingSeconds = payload.remainingSeconds
        )
    }

    fun onTick(payload: CooldownTickPayload) {
        val key = payload.key.toCooldownKey()
        val current = states[key] ?: return
        states[key] = current.copy(remainingSeconds = payload.remainingSeconds)

    }

    fun onChargeRestored(payload: CooldownChargeRestoredPayload) {

    }

    fun onFinished(payload: CooldownFinishedPayload) {
        states.remove(payload.key.toCooldownKey())
    }

    fun onCanceled(payload: CooldownCanceledPayload) {
        states.remove(payload.key.toCooldownKey())
    }

    fun getProgress(key: CooldownKey): Float {
        return states[key]?.progress ?: 0f
    }

    fun hasCooldown(key: CooldownKey): Boolean = states.containsKey(key)
}
