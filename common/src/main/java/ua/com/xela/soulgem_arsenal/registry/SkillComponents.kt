package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.core.component.DataComponentType
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.feature.sword.strength.StrengthSkill
import kotlin.reflect.KClass

interface ComponentRef<T> {
    fun get(): DataComponentType<T>
}

object SkillComponents {

    lateinit var Teleport: ComponentRef<TeleportSkill>
        private set

    lateinit var ArrowsHail: ComponentRef<ArrowsHailSkill>
        private set

    lateinit var Strength: ComponentRef<StrengthSkill>
        private set


    lateinit var skills: Map<KClass<out Skill>, ComponentRef<out Skill>>

    operator fun get(clazz: KClass<out Skill>): ComponentRef<out Skill>? {
        return skills[clazz]
    }

    fun register() {
        Teleport = Services.PLATFORM.registerDataComponent(
            "bow_teleport_skill",
            TeleportSkill.CODEC,
            TeleportSkill.STREAM_CODEC
        )
        ArrowsHail = Services.PLATFORM.registerDataComponent(
            "bow_arrows_hail_skill",
            ArrowsHailSkill.CODEC,
            ArrowsHailSkill.STREAM_CODEC
        )
        Strength = Services.PLATFORM.registerDataComponent(
            "sword_strength_skill",
            StrengthSkill.CODEC,
            StrengthSkill.STREAM_CODEC
        )

        skills = buildMap {
            put(TeleportSkill::class, Teleport)
            put(ArrowsHailSkill::class, ArrowsHail)
            put(StrengthSkill::class, Strength)
        }

    }
}
