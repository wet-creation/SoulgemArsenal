package ua.com.xela.soulgem_arsenal.skills.bow

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class TeleportSkill(
    val enabled: Boolean,
    val charges: Int
) {
    companion object {
        val CODEC: Codec<TeleportSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("enabled").forGetter(TeleportSkill::enabled),
                Codec.INT.fieldOf("charges").forGetter(TeleportSkill::charges)
            ).apply(inst, ::TeleportSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TeleportSkill> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.enabled)
                    buf.writeVarInt(value.charges)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val charges = buf.readVarInt()
                    TeleportSkill(enabled, charges)
                }
            )
    }
}