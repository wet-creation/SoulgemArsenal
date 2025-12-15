package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.core.particles.SimpleParticleType
import ua.com.xela.soulgem_arsenal.platform.api.Services

interface DeferredParticle {
    fun get(): SimpleParticleType
}

object ParticleRegister {
    lateinit var ChaoticParticle: DeferredParticle

    fun registry() {
        ChaoticParticle = Services.PLATFORM.registerSimpleParticle("teleport_particle")
    }

}