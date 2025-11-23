package ua.com.xela.soulgem_arsenal.registry

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.platform.Platform
import ua.com.xela.soulgem_arsenal.platform.Services

object KeyBinds {
    val ActivateSkill: KeyMapping by lazy {
        Services.PLATFORM.registerKeyBinds(
            category = "keybinding_category".toCategory(),
            inputType = InputConstants.Type.KEYSYM,
            key = GLFW.GLFW_KEY_V,
            localizeName = "activate_skill".toName()
        )
    }
    fun registry() {
        if (Services.PLATFORM.platformName == Platform.Forge) return
        ActivateSkill
    }

    private fun String.toName(): String {
        return "key.${Constants.MOD_ID}.${this}"
    }
    private fun String.toCategory(): String {
        return "key.categories.${Constants.MOD_ID}.${this}"
    }
}