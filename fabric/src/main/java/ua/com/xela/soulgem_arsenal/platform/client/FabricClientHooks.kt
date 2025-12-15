package ua.com.xela.soulgem_arsenal.platform.client

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.*
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.platform.client.gui.ClientCooldownHud
import ua.com.xela.soulgem_arsenal.platform.client.system.ClientCooldownTracker
import ua.com.xela.soulgem_arsenal.platform.client.gui.SkillGUI
import ua.com.xela.soulgem_arsenal.platform.client.gui.SkillTooltipClientComponent
import ua.com.xela.soulgem_arsenal.platform.client.gui.SkillTooltipComponent
import ua.com.xela.soulgem_arsenal.registry.KeyBinds
import ua.com.xela.soulgem_arsenal.skills.base.getAllSkills

object FabricClientHooks {
    fun init() {
        KeyBinds.register()
        registerReceivers()
    }

    private fun sendActiveSkill() {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK_CLIENT.sendActivateSkill()
            }
        }
    }

    private fun tooltipRender() {
        TooltipComponentCallback.EVENT.register { data ->
            when (data) {
                is SkillTooltipComponent -> SkillTooltipClientComponent(data)
                else -> null
            }
        }
        ItemTooltipCallback.EVENT.register(ItemTooltipCallback { stack, _, _, lines ->
            if (stack.isEmpty)  return@ItemTooltipCallback
            val skills = stack.getAllSkills()
            if (skills.isEmpty()) return@ItemTooltipCallback

            lines.add(Component.empty())
            skills.forEach { skill ->
                val key = skill.key
                val component = SkillGUI.getSkillGUIComponents(key) ?: return@ItemTooltipCallback


                lines.add(
                    Component.literal("")
                        .append(Component.literal("${component.symbol} ").withStyle(component.symbolColor))
                        .append(component.name.copy().withStyle(component.titleColor))
                )

                lines.add(
                    component.description.copy().withStyle(component.descColor)
                )
            }


        })
    }

    private fun cooldownStart() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownStartPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onStart(payload)
            }
        }

    }

    private fun guiRender() {
        HudRenderCallback.EVENT.register { matrices, delta ->
            val mc = Minecraft.getInstance()
            val window = mc.window
            ClientCooldownHud.render(matrices, window)
        }
    }

    private fun cooldownCanceled() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownCanceledPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onCanceled(payload)
            }
        }
    }

    private fun cooldownChargeRestored() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownChargeRestoredPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onChargeRestored(payload)
            }
        }
    }

    private fun cooldownFinished() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownFinishedPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onFinished(payload)
            }
        }
    }

    private fun cooldownTick() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownTickPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onTick(payload)
            }
        }
    }


    private fun registerReceivers() {
        cooldownCanceled()
        cooldownStart()
        cooldownChargeRestored()
        cooldownFinished()
        sendActiveSkill()
        cooldownTick()
        guiRender()
        tooltipRender()
    }
}
