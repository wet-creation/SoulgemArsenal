package ua.com.xela.soulgem_arsenal.core.coroutines

import net.minecraft.client.Minecraft

object MinecraftScope : BaseMinecraftScope("Soulgem-Client") {

    fun attachDispatcher(client: Minecraft) {
        setDispatcher(client.asCoroutineDispatcher())
    }
}
