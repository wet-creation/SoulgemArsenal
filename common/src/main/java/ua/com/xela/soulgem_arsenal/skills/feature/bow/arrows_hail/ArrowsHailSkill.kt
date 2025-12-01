package ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import ua.com.xela.soulgem_arsenal.skills.api.Skill

data class ArrowsHailSkill(
    override val selected: Boolean,
    override val armed: Boolean,
    override val maxCharges: Int,
    override val charges: Int,
    override val maxCooldown: Int
) : Skill {
    companion object {
        val KEY = Skill.createKey(ArrowsHailSkill::class)

        val CODEC: Codec<ArrowsHailSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("selected").forGetter(ArrowsHailSkill::selected),
                Codec.BOOL.fieldOf("armed").forGetter(ArrowsHailSkill::armed),
                Codec.INT.fieldOf("charges").forGetter(ArrowsHailSkill::charges),
                Codec.INT.fieldOf("maxCharges").forGetter(ArrowsHailSkill::maxCharges),
                Codec.INT.fieldOf("maxCooldown").forGetter(ArrowsHailSkill::maxCooldown),
            ).apply(inst, ::ArrowsHailSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ArrowsHailSkill> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.selected)
                    buf.writeBoolean(value.armed)
                    buf.writeVarInt(value.charges)
                    buf.writeVarInt(value.maxCharges)
                    buf.writeVarInt(value.maxCooldown)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val armed = buf.readBoolean()
                    val charges = buf.readVarInt()
                    val maxCharges = buf.readVarInt()
                    val maxCooldown = buf.readVarInt()
                    ArrowsHailSkill(enabled, armed, charges, maxCharges, maxCooldown)
                }
            )
    }
}