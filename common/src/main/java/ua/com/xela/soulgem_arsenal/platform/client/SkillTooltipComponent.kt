package ua.com.xela.soulgem_arsenal.platform.client

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.tooltip.TooltipComponent

data class SkillTooltipComponent(
    val icon: ResourceLocation,
    val symbolColor: ChatFormatting = ChatFormatting.GOLD,
    val symbol: String = "»",
    val titleColor: ChatFormatting = ChatFormatting.GOLD,
    val name: Component,
    val descColor: ChatFormatting = ChatFormatting.GRAY,
    val description: Component
) : TooltipComponent