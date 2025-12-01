package ua.com.xela.soulgem_arsenal

import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.network.ForgeNetwork
import ua.com.xela.soulgem_arsenal.network.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.platform.ForgePlatformHelper
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.registry.CommandsRegister
import ua.com.xela.soulgem_arsenal.registry.KeyBinds
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager


@Mod(Constants.MOD_ID)
object ForgeMod {

    init {
        ForgePlatformHelper.register(MOD_BUS)
        Common.init()

        MOD_BUS.addListener(::onCommonSetup)
        ForgeNetwork.registerReceivers()
        runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                MOD_BUS.addListener(::registerBindings)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            }
        )
    }


    private fun onClientSetup(event: FMLClientSetupEvent) {
        MinecraftForge.EVENT_BUS.addListener(::onClientTick)
    }

    private fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            MinecraftForge.EVENT_BUS.addListener(::onServerInit)
            MinecraftForge.EVENT_BUS.addListener(::onPlayerLogin)
            MinecraftForge.EVENT_BUS.addListener(::onRegisterCommands)
        }
    }


    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {

    }


    fun onRegisterCommands(event: net.minecraftforge.event.RegisterCommandsEvent) {
        CommandsRegister.register(event.dispatcher)
    }

    private fun registerBindings(event: RegisterKeyMappingsEvent) {
        event.register(KeyBinds.ActivateSkill)
    }

    private fun onClientTick(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            val mc = Minecraft.getInstance()
            if (mc.player == null) return
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK_CLIENT.sendActivateSkill()
            }
        }
    }

    private fun onServerInit(event: LevelEvent.Load) {
        val level = event.level
        if (level is ServerLevel) {
            CooldownStorage.get(level)
        }
    }

    private fun onPlayerLogin(event: PlayerEvent) {
        val player = event.entity as? ServerPlayer ?: return
        when (event) {
            is PlayerEvent.PlayerLoggedInEvent -> {
                SkillCooldownManager.resumeForPlayer(
                    level = player.serverLevel(),
                    player = player
                )
            }
            is PlayerEvent.PlayerLoggedOutEvent -> {
                SkillCooldownManager.onPlayerDisconnect(
                    level = player.serverLevel(),
                    player = player
                )
            }
        }


    }


}
