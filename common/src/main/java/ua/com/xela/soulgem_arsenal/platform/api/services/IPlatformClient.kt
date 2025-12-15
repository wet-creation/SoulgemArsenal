package ua.com.xela.soulgem_arsenal.platform.api.services

import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import ua.com.xela.soulgem_arsenal.registry.DeferredParticle

interface IPlatformClient {

    fun registerSimpleParticleFactory(
        particle: DeferredParticle,
        factory: (sprite: SpriteSet) -> ParticleProvider<SimpleParticleType>
    )
}