package ua.com.xela.soulgem_arsenal.registries

import net.minecraft.core.component.DataComponentType
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.skills.bow.TeleportSkill

interface ComponentRef<T> {
    fun get(): DataComponentType<T>
}

object SkillComponents {

    lateinit var Teleport: ComponentRef<TeleportSkill>
        private set
    fun register() {
        Teleport = Services.PLATFORM.registerDataComponent(
            "bow_skill_teleport",
            TeleportSkill.CODEC,
            TeleportSkill.STREAM_CODEC
        )
    }
}
