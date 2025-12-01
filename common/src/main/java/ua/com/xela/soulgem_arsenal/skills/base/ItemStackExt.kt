package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registry.ItemIdComponents
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import java.util.UUID
import kotlin.reflect.KClass

// common
fun ItemStack.getAllSkills(): List<Skill> {
    val result = mutableListOf<Skill>()

    SkillComponents.skills.values.forEach { value ->
        val skill = get(value.get())
        if (skill != null) {
            result += skill
        }
    }

    return result
}

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

fun ItemStack.getOrCreateInstanceId(): ItemInstanceId {
    val existing = this.get(ItemIdComponents.ITEM_ID.get())
    if (existing != null) return existing

    val newId = ItemInstanceId(UUID.randomUUID())
    this.set(ItemIdComponents.ITEM_ID.get(), newId)
    return newId
}

fun ItemStack.getInstanceIdOrNull(): ItemInstanceId? {
    return this.get(ItemIdComponents.ITEM_ID.get())
}



