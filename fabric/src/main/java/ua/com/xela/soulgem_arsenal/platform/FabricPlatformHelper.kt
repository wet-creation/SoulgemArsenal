package ua.com.xela.soulgem_arsenal.platform

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.KeyMapping
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.platform.api.Platform
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformHelper
import ua.com.xela.soulgem_arsenal.registry.ComponentRef


class FabricPlatformHelper : IPlatformHelper {

    override val platformName: Platform
        get() = Platform.Fabric

    override fun isModLoaded(modId: String?): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }

    override val isDevelopmentEnvironment: Boolean
        get() = FabricLoader.getInstance().isDevelopmentEnvironment

    override fun registerItem(id: String, item: () -> Item) {
        val itemKey = ResourceKey.create(Registries.ITEM, Common.createResourceLocation(id))
        Registry.register(BuiltInRegistries.ITEM, itemKey, item())
    }

    override fun <T> registerDataComponent(
        id: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
    ): ComponentRef<T> {
        val type = DataComponentType.builder<T>()
            .persistent(codec)
            .networkSynchronized(streamCodec)
            .build()
        val key = ResourceKey.create(
            Registries.DATA_COMPONENT_TYPE,
            Common.createResourceLocation(id)
        )
        val registered = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            key,
            type
        )

        return object : ComponentRef<T> {
            override fun get(): DataComponentType<T> {
                return registered
            }

        }
    }

    override fun registerKeyBinds(
        category: String,
        inputType: InputConstants.Type,
        key: Int,
        localizeName: String
    ): KeyMapping {
        val bind = KeyMapping(
            localizeName,
            inputType,
            key,
            category
        )
        KeyBindingHelper.registerKeyBinding(bind)

        return bind
    }
}
