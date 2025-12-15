package ua.com.xela.soulgem_arsenal.platform

import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import ua.com.xela.soulgem_arsenal.registry.DeferredParticle

object ForgeParticlesPlatform {
    val pendingSpriteParticles =
        mutableListOf<Pair<DeferredParticle, (SpriteSet) -> ParticleProvider<SimpleParticleType>>>()

}