package ua.com.xela.soulgem_arsenal.platform

import net.minecraft.client.particle.SpriteSet
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import ua.com.xela.soulgem_arsenal.core.Constants.MOD_ID
import ua.com.xela.soulgem_arsenal.platform.client.gui.SkillTooltipClientComponent
import ua.com.xela.soulgem_arsenal.platform.client.gui.SkillTooltipComponent

@Mod.EventBusSubscriber(
    modid = MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object SoulgemClientRegistry {

    @SubscribeEvent
    fun registerTooltipFactories(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(
            SkillTooltipComponent::class.java
        ) { data -> SkillTooltipClientComponent(data) }
    }

    @SubscribeEvent
    fun onRegisterParticleProviders(event: RegisterParticleProvidersEvent) {
        ForgeParticlesPlatform.pendingSpriteParticles.forEach { (type, factory) ->
            event.registerSpriteSet(type.get()) { sprites: SpriteSet ->
                factory(sprites)
            }
        }
    }



}
