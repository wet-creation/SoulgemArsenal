package ua.com.xela.soulgem_arsenal.skills.base

import ua.com.xela.soulgem_arsenal.skills.api.Skill
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun Skill.toDebugString(): String {
    val kClass = this::class as KClass<Skill>
    val props = kClass.memberProperties

    val fields = props.joinToString(", ") { prop ->
        val value = try {
            prop.get(this)
        } catch (e: Exception) {
            "<error: ${e.message}>"
        }
        "${prop.name}=$value"
    }

    return "${kClass.simpleName}($fields)"
}