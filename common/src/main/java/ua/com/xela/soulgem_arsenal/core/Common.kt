package ua.com.xela.soulgem_arsenal.core

import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.registry.*
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowHailSkillBehavior
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillBehavior
import ua.com.xela.soulgem_arsenal.skills.system.SkillBehaviorRegistry


object Common {
    private val keys: MutableSet<String> = mutableSetOf()

    fun init() {
        ModItems.register()
        SkillComponents.register()
        ItemIdComponents.register()
        registerSkillBehaviors()
        ParticleRegister.registry()

    }

    private fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
        SkillBehaviorRegistry.register(ArrowHailSkillBehavior)
    }

    fun clientRegistry() {
        ParticleFactory.registry()
    }


    fun createResourceLocation(name: String): ResourceLocation {
        check(keys.add(name)) {
            "Resource $name is already existed"
        }
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)
    }

    fun getResourceLocation(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)
    }


}
