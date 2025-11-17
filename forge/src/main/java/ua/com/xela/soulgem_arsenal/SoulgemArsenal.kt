package ua.com.xela.soulgem_arsenal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist
import kotlin.time.Duration.Companion.seconds

@Mod(Constants.MOD_ID)
object SoulgemArsenal {
    // the logger for our mod
    val LOGGER: Logger = Constants.LOG

    init {
        LOGGER.info("Hello world!")
        Common.init()
        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        println(obj)

        @Serializable
        data class MySerializedThing(
            val name: String,
            val number: Int
        )

        val testObject = MySerializedThing("KotlinForForge", 712)
        val json = Json.encodeToString(testObject)
        LOGGER.info("--- JSON: $json")

        // sample coroutines code
        CoroutineScope(Dispatchers.Default).launch {
            LOGGER.info("Before delay")
            delay(5.seconds)
            LOGGER.info("After 5 seconds")
        }
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.info("Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.info("Server starting...")
    }
}