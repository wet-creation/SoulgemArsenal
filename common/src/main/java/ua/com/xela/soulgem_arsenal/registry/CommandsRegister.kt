package ua.com.xela.soulgem_arsenal.registry

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.skills.base.getAllSkills
import ua.com.xela.soulgem_arsenal.skills.base.toDebugString

object CommandsRegister {


    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        registerDebugSkillsCommand(dispatcher)
    }


    private fun registerDebugSkillsCommand(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("debugskills")
                .requires { source -> source.entity is ServerPlayer } // только игрок
                .executes { ctx ->
                    val source = ctx.source
                    val player = source.playerOrException
                    val stack = player.mainHandItem

                    if (stack.isEmpty) {
                        source.sendSuccess(
                            { Component.literal("В основной руке пусто.") },
                            false
                        )
                        return@executes 0
                    }

                    val skills = stack.getAllSkills()

                    if (skills.isEmpty()) {
                        source.sendSuccess(
                            { Component.literal("У этого предмета нет скиллов.") },
                            false
                        )
                        return@executes 1
                    }

                    source.sendSuccess(
                        { Component.literal("Скиллы предмета:") },
                        false
                    )

                    skills.forEach { skill ->
                        source.sendSuccess(
                            { Component.literal(" - ${skill.toDebugString()}") },
                            false
                        )
                    }

                    1
                }
        )
    }


}