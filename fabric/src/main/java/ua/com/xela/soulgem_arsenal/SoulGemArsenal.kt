package ua.com.xela.soulgem_arsenal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.api.ModInitializer
import ua.com.xela.soulgem_arsenal.Constants.LOG
import kotlin.time.Duration.Companion.seconds

object SoulGemArsenal : ModInitializer {

    override fun onInitialize() {
        Common.init()
        LOG.info("Hello world!")
        @Serializable
        data class MySerializedThing(
            val name: String,
            val number: Int
        )

        val testObject = MySerializedThing("Fabric bem bem", 712)
        val json = Json.encodeToString(testObject)
        LOG.info("--- JSON: $json")

        // sample coroutines code
        CoroutineScope(Dispatchers.Default).launch {
            LOG.info("Before delay")
            delay(5.seconds)
            LOG.info("After 5 seconds")
        }
    }
}