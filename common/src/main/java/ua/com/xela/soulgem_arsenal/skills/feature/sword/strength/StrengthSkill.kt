package ua.com.xela.soulgem_arsenal.skills.feature.sword.strength

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey

data class StrengthSkill(
    override val selected: Boolean,
    override val armed: Boolean,
    override val charges: Int,
    override val maxCharges: Int,
    override val maxCooldown: Int,
    val amplifier: Int,
    val durationTicks: Int
) : Skill {

    override val key: SkillKey
        get() = KEY


    companion object {
        val KEY = Skill.createKey(StrengthSkill::class)

        val CODEC: Codec<StrengthSkill> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.BOOL.fieldOf("selected").forGetter(StrengthSkill::selected),
                Codec.BOOL.fieldOf("armed").forGetter(StrengthSkill::armed),
                Codec.INT.fieldOf("charges").forGetter(StrengthSkill::charges),
                Codec.INT.fieldOf("maxCharges").forGetter(StrengthSkill::maxCharges),
                Codec.INT.fieldOf("maxCooldown").forGetter(StrengthSkill::maxCooldown),
                Codec.INT.fieldOf("amplifier").forGetter(StrengthSkill::amplifier),
                Codec.INT.fieldOf("durationTicks").forGetter(StrengthSkill::durationTicks),
            ).apply(inst, ::StrengthSkill)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, StrengthSkill> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeBoolean(value.selected)
                    buf.writeBoolean(value.armed)
                    buf.writeVarInt(value.charges)
                    buf.writeVarInt(value.maxCharges)
                    buf.writeVarInt(value.maxCooldown)
                    buf.writeVarInt(value.amplifier)
                    buf.writeVarInt(value.durationTicks)
                },
                { buf ->
                    val enabled = buf.readBoolean()
                    val armed = buf.readBoolean()
                    val charges = buf.readVarInt()
                    val maxCharges = buf.readVarInt()
                    val maxCooldown = buf.readVarInt()
                    val amplifier = buf.readVarInt()
                    val durationTicks = buf.readVarInt()
                    StrengthSkill(
                        selected = enabled,
                        armed = armed,
                        charges = charges,
                        maxCharges = maxCharges,
                        maxCooldown = maxCooldown,
                        amplifier = amplifier,
                        durationTicks = durationTicks
                    )
                }
            )
    }
}