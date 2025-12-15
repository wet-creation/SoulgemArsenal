package ua.com.xela.soulgem_arsenal.platform

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Codec
import net.minecraft.client.KeyMapping
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import thedarkcolour.kotlinforforge.forge.DIST
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.platform.api.Platform
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformHelper
import ua.com.xela.soulgem_arsenal.registry.ComponentRef
import ua.com.xela.soulgem_arsenal.registry.DeferredParticle


class ForgePlatformHelper : IPlatformHelper {
    companion object Registry {
        val items: DeferredRegister<Item?> =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID)
        val dataComponents: DeferredRegister<DataComponentType<*>> =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID)
        val particles: DeferredRegister<ParticleType<*>> =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Constants.MOD_ID)


        fun register(eventBus: IEventBus) {
            items.register(eventBus)
            dataComponents.register(eventBus)
            particles.register(eventBus)
        }
    }

    override val isClientEnv: Boolean
        get() = DIST.isClient

    override val platformName: Platform
        get() = Platform.Forge

    override fun isModLoaded(modId: String?): Boolean {
        return ModList.get().isLoaded(modId)
    }

    override val isDevelopmentEnvironment: Boolean
        get() = !FMLLoader.isProduction()

    override fun registerItem(id: String, item: () -> Item) {
        items.register(id, item)
    }

    override fun <T> registerDataComponent(
        id: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
    ): ComponentRef<T> {
        val regObj: RegistryObject<DataComponentType<T>> =
            dataComponents.register(id) {
                DataComponentType.builder<T>()
                    .persistent(codec)
                    .networkSynchronized(streamCodec)
                    .build()
            }

        return object : ComponentRef<T> {
            override fun get(): DataComponentType<T> {
                return regObj.get()
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

        return bind
    }

    override fun registerSimpleParticle(
        id: String,
    ): DeferredParticle {
        val registered = particles.register(id) {
            SimpleParticleType(false)
        }
        return object : DeferredParticle {
            override fun get(): SimpleParticleType {
                return registered.get()
            }
        }
    }
}
