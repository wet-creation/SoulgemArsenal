package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level

class ParticleBuilder {
    lateinit var level: Level
    lateinit var particleType: SimpleParticleType

    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    var count: Int = 1
    var xOffset: Double = 0.3
    var yOffset: Double = 0.3
    var zOffset: Double = 0.3

    var speed: Double = 0.08

    var clientOnly: Boolean = false
}

fun summonParticle(block: ParticleBuilder.() -> Unit) {
    val b = ParticleBuilder().apply(block)

    if (b.clientOnly) {
        if (!b.level.isClientSide) return
        val cl = b.level as ClientLevel

        repeat(b.count) {
            val px = b.x + (cl.random.nextDouble() - 0.5) * b.xOffset * 2
            val py = b.y + (cl.random.nextDouble() - 0.5) * b.yOffset * 2
            val pz = b.z + (cl.random.nextDouble() - 0.5) * b.zOffset * 2

            val vx = (cl.random.nextDouble() - 0.5) * b.speed * 2
            val vy = (cl.random.nextDouble() - 0.5) * b.speed * 2
            val vz = (cl.random.nextDouble() - 0.5) * b.speed * 2

            cl.addParticle(b.particleType,
                px, py, pz,
                vx, vy, vz
            )
        }
        return
    }

    val sl = b.level as ServerLevel
    sl.sendParticles(
        b.particleType,
        b.x, b.y, b.z,
        b.count,
        b.xOffset, b.yOffset, b.zOffset,
        b.speed
    )
}

