package ua.com.xela.soulgem_arsenal.platform.client

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformClient
import ua.com.xela.soulgem_arsenal.registry.DeferredParticle

class FabricClientPlatform: IPlatformClient {
    override fun registerSimpleParticleFactory(
        particle: DeferredParticle,
        factory: (sprite: SpriteSet) -> ParticleProvider<SimpleParticleType>
    ) {
        ParticleFactoryRegistry.getInstance()
            .register(particle.get().type) { sprites: SpriteSet ->
                factory(sprites)
            }
    }

}