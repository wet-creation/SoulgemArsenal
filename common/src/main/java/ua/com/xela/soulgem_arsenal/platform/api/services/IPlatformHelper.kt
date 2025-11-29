package ua.com.xela.soulgem_arsenal.platform.api.services

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Codec
import net.minecraft.client.KeyMapping
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import ua.com.xela.soulgem_arsenal.platform.api.Platform
import ua.com.xela.soulgem_arsenal.registry.ComponentRef

interface IPlatformHelper {
    val platformName: Platform

    val isDevelopmentEnvironment: Boolean

    val environmentName: String
        get() = if (this.isDevelopmentEnvironment) "development" else "production"


    fun isModLoaded(modId: String?): Boolean

    fun registerItem(id: String, item: () -> Item)

    fun <T> registerDataComponent(
        id: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
    ): ComponentRef<T>

    fun registerKeyBinds(
        category: String,
        inputType: InputConstants.Type,
        key: Int,
        localizeName: String

    ): KeyMapping
}
