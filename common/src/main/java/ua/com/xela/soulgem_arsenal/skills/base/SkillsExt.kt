package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import kotlin.reflect.KClass

inline fun <reified T : Skill> ItemStack.getSkill(): T? =
    getSkill(T::class)

fun <T : Skill> ItemStack.getSkill(klass: KClass<T>): T? {
    val ref = SkillComponents[klass] ?: return null
    @Suppress("UNCHECKED_CAST")
    return this.get(ref.get()) as? T
}

inline fun <reified T : Skill> ItemStack.setSkill(newState: T) {
    setSkill(T::class, newState)
}

fun <T : Skill> ItemStack.setSkill(clazz: KClass<T>, newState: T) {
    val componentRef = SkillComponents[clazz] ?: return
    @Suppress("UNCHECKED_CAST")
    val component = componentRef.get() as? DataComponentType<T> ?: return
    this.set(component, newState)
}


fun ItemStack.setSkillDynamic(clazz: KClass<out Skill>, newState: Skill) {
    val ref = SkillComponents[clazz] ?: return

    @Suppress("UNCHECKED_CAST")
    val componentAny = ref.get() as DataComponentType<Any>

    @Suppress("UNCHECKED_CAST")
    this.set(componentAny, newState as Any)

}



