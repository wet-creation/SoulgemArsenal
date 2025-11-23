package ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import ua.com.xela.soulgem_arsenal.skills.api.Skill

data class ArrowsHail(
    override val selected: Boolean,
    override val armed: Boolean,
    override val cooldown: Int,
    override val charges: Int,
    override val maxCooldown: Int
) : Skill {
    companion object {
        val CODEC: Codec<ArrowsHail> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("selected").forGetter(ArrowsHail::selected),
                Codec.BOOL.fieldOf("armed").forGetter(ArrowsHail::armed),
                Codec.INT.fieldOf("charges").forGetter(ArrowsHail::charges),
                Codec.INT.fieldOf("cooldown").forGetter(ArrowsHail::cooldown),
                Codec.INT.fieldOf("maxCooldown").forGetter(ArrowsHail::maxCooldown),
            ).apply(inst, ::ArrowsHail)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ArrowsHail> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.selected)
                    buf.writeBoolean(value.armed)
                    buf.writeVarInt(value.charges)
                    buf.writeVarInt(value.cooldown)
                    buf.writeVarInt(value.maxCooldown)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val armed = buf.readBoolean()
                    val charges = buf.readVarInt()
                    val cooldown = buf.readVarInt()
                    val maxCooldown = buf.readVarInt()
                    ArrowsHail(enabled, armed, charges, cooldown, maxCooldown)
                }
            )
    }
}