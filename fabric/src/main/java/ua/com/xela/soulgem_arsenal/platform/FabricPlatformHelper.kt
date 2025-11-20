package ua.com.xela.soulgem_arsenal.platform

import com.mojang.serialization.Codec
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import ua.com.xela.soulgem_arsenal.Constants
import ua.com.xela.soulgem_arsenal.platform.services.IPlatformHelper
import ua.com.xela.soulgem_arsenal.registries.ComponentRef
import ua.com.xela.soulgem_arsenal.skills.Skill


class FabricPlatformHelper : IPlatformHelper {

    override val platformName: String
        get() = "Fabric"

    override fun isModLoaded(modId: String?): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }

    override val isDevelopmentEnvironment: Boolean
        get() = FabricLoader.getInstance().isDevelopmentEnvironment

    override fun registerItem(id: String, item: () -> Item) {
        val itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id))
        Registry.register(BuiltInRegistries.ITEM, itemKey, item())
    }

    override fun <T : Skill> registerDataComponent(
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
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id)
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
}

