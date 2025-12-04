package ua.com.xela.soulgem_arsenal.platform

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import ua.com.xela.soulgem_arsenal.core.Constants.MOD_ID
import ua.com.xela.soulgem_arsenal.platform.client.SkillTooltipClientComponent
import ua.com.xela.soulgem_arsenal.platform.client.SkillTooltipComponent

@Mod.EventBusSubscriber(
    modid = MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object SoulgemTooltipFactoryRegistry {

    @SubscribeEvent
    fun registerTooltipFactories(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(
            SkillTooltipComponent::class.java
        ) { data -> SkillTooltipClientComponent(data) }
    }
}
