package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill


data class TeleportSkill(
    override val selected: Boolean,
    override val armed: Boolean,
    override val charges: Int,
    override val maxCharges: Int,
    override val maxCooldown: Int
) : Skill {

    val armedOrEnabled = selected || armed
    val armedAndEnabled = selected && armed

    override val key: SkillKey
        get() = KEY


    companion object {
        val KEY = Skill.createKey(TeleportSkill::class)

        val CODEC: Codec<TeleportSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("selected").forGetter(TeleportSkill::selected),
                Codec.BOOL.fieldOf("armed").forGetter(TeleportSkill::armed),
                Codec.INT.fieldOf("charges").forGetter(TeleportSkill::charges),
                Codec.INT.fieldOf("maxCharges").forGetter(TeleportSkill::maxCharges),
                Codec.INT.fieldOf("maxCooldown").forGetter(TeleportSkill::maxCooldown),
            ).apply(inst, ::TeleportSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TeleportSkill> =
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
                    TeleportSkill(
                        selected = enabled,
                        armed = armed,
                        charges = charges,
                        maxCharges = maxCharges,
                        maxCooldown = maxCooldown
                    )
                }
            )
    }
}