package ua.com.xela.soulgem_arsenal.platform.client.system.particles.chaotic

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle

class ChaoticParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    vx: Double,
    vy: Double,
    vz: Double,
    sprites: SpriteSet
) : TextureSheetParticle(level, x, y, z, vx, vy, vz) {

    init {
        lifetime = 20
        quadSize = 0.5f
        pickSprite(sprites)
    }

    override fun tick() {
        super.tick()
        x += xd
        y += yd
        z += zd

        // затухание
        val t = age.toFloat() / lifetime.toFloat()
        quadSize = 0.2f * (1f - t)
        alpha = 1f - t

        if (age >= lifetime) {
            remove()
        }

    }

    override fun getRenderType(): ParticleRenderType =
        ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
}