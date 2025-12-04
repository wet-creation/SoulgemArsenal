package ua.com.xela.soulgem_arsenal.skills.api

import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import kotlin.reflect.KClass

interface Skill {
    //player selected
    val selected: Boolean

    //is ready for use
    val armed: Boolean

    //max charges on item
    val maxCharges: Int

    //cooldown in seconds for next recharge
    val maxCooldown: Int

    //count of charges
    val charges: Int

    val key: SkillKey


    companion object {
        fun <T : Any> createKey(clazz: KClass<T>): SkillKey {
            return SkillKey(
                Common.createResourceLocation(
                    clazz.simpleName?.lowercase() ?: error("Couldn't create id, simpleName is NUll for $clazz")
                )
            )
        }
    }
}