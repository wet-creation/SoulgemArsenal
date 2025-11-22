package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import ua.com.xela.soulgem_arsenal.skills.api.Skill


data class TeleportSkill(
    override val enabled: Boolean,
    override val armed: Boolean,
    override val charges: Int,
    override val cooldown: Int
) : Skill {

    val armedOrEnabled = enabled || armed
    val armedAndEnabled = enabled && armed

    class Builder(origin: TeleportSkill) {
        var enabled: Boolean = origin.enabled
        var armed: Boolean = origin.armed
        var charges: Int = origin.charges
        var cooldown: Int = origin.cooldown

        fun enabled(p: Boolean) = this.also { enabled = p }
        fun charges(p: Int) = this.also { charges = p }
        fun cooldown(p: Int) = this.also { cooldown = p }
        fun armed(p: Boolean) = this.also { armed = p }

        fun build(): TeleportSkill {
            return TeleportSkill(
                enabled = enabled,
                armed = armed,
                charges = charges,
                cooldown = cooldown
            )
        }
    }

    companion object {
        val CODEC: Codec<TeleportSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("enabled").forGetter(TeleportSkill::enabled),
                Codec.BOOL.fieldOf("armed").forGetter(TeleportSkill::armed),
                Codec.INT.fieldOf("charges").forGetter(TeleportSkill::charges),
                Codec.INT.fieldOf("cooldown").forGetter(TeleportSkill::cooldown),
            ).apply(inst, ::TeleportSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TeleportSkill> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.enabled)
                    buf.writeBoolean(value.armed)
                    buf.writeVarInt(value.charges)
                    buf.writeVarInt(value.cooldown)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val armed = buf.readBoolean()
                    val charges = buf.readVarInt()
                    val cooldown = buf.readVarInt()
                    TeleportSkill(enabled, armed, charges, cooldown)
                }
            )
    }
}