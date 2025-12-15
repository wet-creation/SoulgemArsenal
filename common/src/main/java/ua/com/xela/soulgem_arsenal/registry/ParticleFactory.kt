package ua.com.xela.soulgem_arsenal.registry

import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.platform.client.system.particles.chaotic.ChaoticParticleFactory

object ParticleFactory {
    lateinit var ChaoticParticleFactory: ChaoticParticleFactory

    fun registry() {

        Services.CLIENT_PLATFORM.registerSimpleParticleFactory(ParticleRegister.ChaoticParticle) {
            ChaoticParticleFactory = ChaoticParticleFactory(it)
            ChaoticParticleFactory
        }
    }
}