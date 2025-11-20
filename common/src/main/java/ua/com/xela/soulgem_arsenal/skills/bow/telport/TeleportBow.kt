package ua.com.xela.soulgem_arsenal.skills.bow.telport

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import ua.com.xela.soulgem_arsenal.skills.Skill


data class TeleportSkill(
    //means player selected it
    val enabled: Boolean,
    //is ready for use
    val armed: Boolean,
    //count of charges
    val charges: Int
) : Skill {

    val armOrEnabled = enabled || armed
    val armAndEnabled = enabled && armed

    class Builder(origin: TeleportSkill) {
        var enabled: Boolean = origin.enabled
        var armed: Boolean = origin.armed
        var charges: Int = origin.charges

        fun enabled(p: Boolean) = this.also { enabled = p }
        fun charges(p: Int) = this.also { charges = p }
        fun armed(p: Boolean) = this.also { armed = p }

        fun build(): TeleportSkill {
            return TeleportSkill(
                enabled = enabled,
                armed = armed,
                charges = charges
            )
        }
    }

    companion object {
        val CODEC: Codec<TeleportSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("enabled").forGetter(TeleportSkill::enabled),
                Codec.BOOL.fieldOf("armed").forGetter(TeleportSkill::armed),
                Codec.INT.fieldOf("charges").forGetter(TeleportSkill::charges)
            ).apply(inst, ::TeleportSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TeleportSkill> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.enabled)
                    buf.writeBoolean(value.armed)
                    buf.writeVarInt(value.charges)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val armed = buf.readBoolean()
                    val charges = buf.readVarInt()
                    TeleportSkill(enabled, armed, charges)
                }
            )
    }
}