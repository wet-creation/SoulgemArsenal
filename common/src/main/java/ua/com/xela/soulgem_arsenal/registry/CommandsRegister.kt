package ua.com.xela.soulgem_arsenal.registry

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry
import ua.com.xela.soulgem_arsenal.skills.base.getAllSkills
import ua.com.xela.soulgem_arsenal.skills.base.toDebugString
import ua.com.xela.soulgem_arsenal.skills.system.SkillFactory

object CommandsRegister {


    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        if (Services.PLATFORM.isDevelopmentEnvironment.not()) return

        registerDebugSkillsCommand(dispatcher)
        registerSetSkillCommand(dispatcher)
    }


    private fun registerDebugSkillsCommand(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("debugskills")
                .requires { source -> source.entity is ServerPlayer }
                .executes { ctx ->
                    val source = ctx.source
                    val player = source.playerOrException
                    debugItemInHand(source, player)
                    1
                }
                .then(
                    argument("target", EntityArgument.player())
                        .executes { ctx ->
                            val source = ctx.source
                            val target = EntityArgument.getPlayer(ctx, "target")
                            debugItemInHand(source, target)
                            1
                        }
                )
        )
    }

    private fun debugItemInHand(source: CommandSourceStack, player: ServerPlayer) {
        val stack = player.mainHandItem

        if (stack.isEmpty) {
            source.sendSuccess(
                { Component.literal("Player ${player.scoreboardName} has empty hand.") },
                false
            )
            return
        }

        val skills = stack.getAllSkills() // твоя функция

        if (skills.isEmpty()) {
            source.sendSuccess(
                { Component.literal("Player ${player.scoreboardName} does not have any skills on item.") },
                false
            )
            return
        }

        source.sendSuccess(
            { Component.literal("Skills: ${player.scoreboardName}:") },
            false
        )
        skills.forEach { skill ->
            source.sendSuccess(
                { Component.literal(" - ${skill.toDebugString()}") },
                false
            )
        }
    }

    private fun registerSetSkillCommand(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("setskill")
                .requires { source -> source.hasPermission(2) } // или свой уровень прав

                .then(
                    argument("skill", StringArgumentType.word())
                        .executes { ctx ->
                            val source = ctx.source
                            val player = source.playerOrException
                            val skillId = StringArgumentType.getString(ctx, "skill")

                            setSkillForPlayerInMainHand(source, player, skillId)
                        }
                        .then(
                            argument("target", EntityArgument.player())
                                .executes { ctx ->
                                    val source = ctx.source
                                    val skillId = StringArgumentType.getString(ctx, "skill")
                                    val target = EntityArgument.getPlayer(ctx, "target")

                                    setSkillForPlayerInMainHand(source, target, skillId)
                                }
                        )
                )
        )
    }


    private fun setSkillForPlayerInMainHand(
        source: CommandSourceStack,
        player: ServerPlayer,
        skillId: String
    ): Int {
        val stack = player.mainHandItem
        if (stack.isEmpty) {
            source.sendSuccess(
                { Component.literal("Player ${player.scoreboardName} has empty hand.") },
                false
            )
            return 0
        }


        val skill = createSkillById(skillId)
        if (skill == null) {
            source.sendSuccess(
                { Component.literal("Unknown skill: '$skillId'.") },
                false
            )
            return 0
        }
        SkillFactory.applySkillEntry(stack, skill)

        source.sendSuccess(
            { Component.literal("Skill '$skillId' added to player main hand ${player.scoreboardName}.") },
            false
        )

        return 1
    }

}

private fun createSkillById(id: String): SkillEntry<out Skill>? {

    return when (id.lowercase()) {
        "teleport" -> {
            SkillFactory.BowTeleport
        }
        "arrows_hail" -> {
            SkillFactory.BowArrowsHail
        }
        "strength" -> {
            SkillFactory.SwordStrength
        }

        else -> null
    }

}
