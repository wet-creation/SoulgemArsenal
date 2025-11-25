package ua.com.xela.soulgem_arsenal

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.platform.ForgePlatformHelper
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.client.input.KeyBinds


@Mod(Constants.MOD_ID)
object ForgeMod {

    init {
        ForgePlatformHelper.register(MOD_BUS)
        Common.init()

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


    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }


    private fun registerBindings(event: RegisterKeyMappingsEvent) {
        event.register(KeyBinds.ActivateSkill)
    }

    private fun onClientTick(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            val mc = Minecraft.getInstance()
            if (mc.player == null) return
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK.sendActivateSkill()
            }
        }
    }
}
