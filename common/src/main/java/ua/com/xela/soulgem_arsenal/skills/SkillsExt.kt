package ua.com.xela.soulgem_arsenal.skills

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registries.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.bow.TeleportSkill

inline fun <reified T : Skill> ItemStack.getSkill(): T? {
    val ref = SkillComponents[T::class] ?: return null
    return this.get(ref.get()) as T?
}

inline fun <reified T : Skill> ItemStack.setSkill(newState: T) {
    @Suppress("UNCHECKED_CAST")
    val component = SkillComponents[T::class]?.get() as? DataComponentType<T> ?: return
    this.set(
        component,
        newState
    )
}


fun ItemStack.hasTeleportSkill(): Boolean {
    val data = getSkill<TeleportSkill>() ?: return false
    return data.enabled && data.charges > 0
}

fun ItemStack.armTeleportSkill(): Boolean {
    val data = getSkill<TeleportSkill>() ?: return false
    if (!data.enabled || data.charges <= 0) return false

    val updated = data.copy(armed = true)
    this.set(SkillComponents.Teleport.get(), updated)
    return true
}

fun ItemStack.disarmTeleportSkill() {
    val data = getSkill<TeleportSkill>() ?: return
    if (!data.armed) return
    this.set(SkillComponents.Teleport.get(), data.copy(armed = false))
}