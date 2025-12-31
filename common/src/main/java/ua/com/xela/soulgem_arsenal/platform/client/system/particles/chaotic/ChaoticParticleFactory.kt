package ua.com.xela.soulgem_arsenal.platform.client.system.particles.chaotic

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType

class ChaoticParticleFactory(
    private val sprites: SpriteSet
) : ParticleProvider<SimpleParticleType> {

    override fun createParticle(
        type: SimpleParticleType,
        level: ClientLevel,
        x: Double,
        y: Double,
        z: Double,
        vx: Double,
        vy: Double,
        vz: Double
    ): Particle {
        return ChaoticParticle(level, x, y, z, vx, vy, vz, sprites)
    }
}