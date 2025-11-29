package ua.com.xela.soulgem_arsenal.core.coroutines

import net.minecraft.server.MinecraftServer

object MinecraftServerScope : BaseMinecraftScope("Soulgem-Server") {

    fun attachDispatcher(server: MinecraftServer) {
        setDispatcher(server.asCoroutineDispatcher())
    }
}