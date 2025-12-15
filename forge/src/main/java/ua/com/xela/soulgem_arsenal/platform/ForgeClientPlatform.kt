package ua.com.xela.soulgem_arsenal.platform

import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformClient
import ua.com.xela.soulgem_arsenal.registry.DeferredParticle

class ForgeClientPlatform : IPlatformClient {
    override fun registerSimpleParticleFactory(
        particle: DeferredParticle,
        factory: (sprite: SpriteSet) -> ParticleProvider<SimpleParticleType>
    ) {
        ForgeParticlesPlatform.pendingSpriteParticles += particle to factory
    }
}