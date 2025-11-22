package ua.com.xela.soulgem_arsenal.platform.services

import com.mojang.serialization.Codec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import ua.com.xela.soulgem_arsenal.registry.ComponentRef
import ua.com.xela.soulgem_arsenal.skills.api.Skill

interface IPlatformHelper {
    val platformName: String

    val isDevelopmentEnvironment: Boolean

    val environmentName: String
        /**
         * Gets the name of the environment type as a string.
         *
         * @return The name of the environment type.
         */
        get() = if (this.isDevelopmentEnvironment) "development" else "production"


    fun isModLoaded(modId: String?): Boolean

    fun registerItem(id: String, item: () -> Item)

    fun <T : Skill> registerDataComponent(
        id: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
    ): ComponentRef<T>
}