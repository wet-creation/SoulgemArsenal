package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.core.component.DataComponentType
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import kotlin.reflect.KClass

interface ComponentRef<T> {
    fun get(): DataComponentType<T>
}

object SkillComponents {

    lateinit var Teleport: ComponentRef<TeleportSkill>
        private set

    lateinit var ArrowsHail: ComponentRef<ArrowsHailSkill>
        private set


    lateinit var skills: Map<KClass<out Skill>, ComponentRef<out Skill>>

    operator fun get(clazz: KClass<out Skill>): ComponentRef<out Skill>? {
        return skills[clazz]
    }

    fun register() {
        Teleport = Services.PLATFORM.registerDataComponent(
            "bow_skill_teleport",
            TeleportSkill.CODEC,
            TeleportSkill.STREAM_CODEC
        )
        ArrowsHail = Services.PLATFORM.registerDataComponent(
            "arrows_hail_skill_teleport",
            ArrowsHailSkill.CODEC,
            ArrowsHailSkill.STREAM_CODEC
        )

        skills = buildMap {
            put(TeleportSkill::class, Teleport)
            put(ArrowsHailSkill::class, ArrowsHail)
        }

    }
}
