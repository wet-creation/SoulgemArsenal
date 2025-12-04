package ua.com.xela.soulgem_arsenal.platform.server

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import ua.com.xela.soulgem_arsenal.skills.base.CooldownKey
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import java.util.UUID
import kotlin.collections.iterator

class CooldownStorage : SavedData() {
    data class CooldownState(
        val totalSecondsPerCharge: Int,
        val remainingSeconds: Int,
        val currentChargeIndex: Int,
        val playerId: UUID,
        val chargeToRestore: Int,
        val endGameTime: Long
    )

    val states: MutableMap<CooldownKey, CooldownState> = mutableMapOf()

    override fun save(tag: CompoundTag, pRegistries: HolderLookup.Provider): CompoundTag {
        val list = ListTag()

        for ((key, state) in states) {
            val entry = CompoundTag()

            entry.putUUID(ITEM_ID, key.itemId.value)

            entry.putString(SKILL_ID_PATH, key.skill.id.toString())

            entry.putUUID(PLAYER_ID, state.playerId)
            entry.putLong(END_GAME_TIME, state.endGameTime)
            entry.putInt(TOTAL_SECONDS_PER_CHARGE, state.totalSecondsPerCharge)
            entry.putInt(REMAINING_SECONDS, state.remainingSeconds)
            entry.putInt(CHARGE_TO_RESTORE, state.chargeToRestore)
            entry.putInt(CURRENT_CHARGE_INDEX, state.currentChargeIndex)

            list.add(entry)
        }

        tag.put(LIST_NAME, list)
        return tag
    }

    companion object {
        private const val LIST_NAME = "Cooldowns"

        //key
        private const val PLAYER_ID = "PlayerId"
        private const val SKILL_ID_PATH = "SkillIdPath"
        private const val ITEM_ID = "ItemId"

        //values
        private const val END_GAME_TIME = "EndGameTime"
        private const val REMAINING_SECONDS = "RemainingSeconds"
        private const val TOTAL_SECONDS_PER_CHARGE = "TotalSeconds"
        private const val CHARGE_TO_RESTORE = "ChargeToRestore"
        private const val CURRENT_CHARGE_INDEX = "ChargeIndexCurrent"


        private const val STORAGE_NAME = "soulegm_arsenal_cooldowns"

        fun get(level: ServerLevel): CooldownStorage {
            val storage = level.dataStorage
            val factory = Factory(
                ::CooldownStorage,
                { tag, _ ->
                    load(tag)
                },
                DataFixTypes.LEVEL

            )
            return storage.computeIfAbsent(
                factory,
                STORAGE_NAME
            )
        }

        private fun load(tag: CompoundTag): CooldownStorage {
            val result = CooldownStorage()
            val list = tag.getList(LIST_NAME, CompoundTag.TAG_COMPOUND.toInt())
            for (element in list) {

                element as CompoundTag

                val itemId = ItemInstanceId(element.getUUID(ITEM_ID))
                val skillId = element.getString(SKILL_ID_PATH)

                val playerId = element.getUUID(PLAYER_ID)
                val endGameTime = element.getLong(END_GAME_TIME)
                val totalSeconds = element.getInt(TOTAL_SECONDS_PER_CHARGE)
                val remainingSeconds = element.getInt(REMAINING_SECONDS)
                val chargeToRestore = element.getInt(CHARGE_TO_RESTORE)
                val index = element.getInt(CURRENT_CHARGE_INDEX)

                val key = CooldownKey(
                    itemId = itemId,
                    skill = SkillKey(ResourceLocation.parse(skillId))
                )

                val state = CooldownState(
                    playerId = playerId,
                    endGameTime = endGameTime,
                    totalSecondsPerCharge = totalSeconds,
                    chargeToRestore = chargeToRestore,
                    currentChargeIndex = index,
                    remainingSeconds = remainingSeconds
                )

                result.states[key] = state
            }

            return result
        }
    }
}