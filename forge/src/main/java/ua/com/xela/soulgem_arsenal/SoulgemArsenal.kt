package ua.com.xela.soulgem_arsenal

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist
import ua.com.xela.soulgem_arsenal.platform.ForgePlatformHelper

@Mod(Constants.MOD_ID)
object SoulgemArsenal {
    val LOGGER: Logger = Constants.LOG

    init {
        LOGGER.info("Hello world!")
        ForgePlatformHelper.register(MOD_BUS)
        Common.init()

        runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            }
        )
    }


    private fun onClientSetup(event: FMLClientSetupEvent) {
    }


    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }
}