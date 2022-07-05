package de.bybackfish.avomod.gui.elements

import de.bybackfish.avomod.features.FeatureData
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import kotlin.math.roundToInt

class FeatureToggleButton
    (val feature: FeatureData, x: Int, y: Int) : GuiButton(
    (Math.random() * 1000).roundToInt(),
    x,
    y, 100,
    20,
    if (feature.enabled) "§aEnabled" else "§cDisabled",
) {


    override fun mouseReleased(mouseX: Int, mouseY: Int) {
        // feature.toggle();
        feature.toggle()
        super.mouseReleased(mouseX, mouseY)
    }

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float) {
        var color = 0xBBBBBB
        if (enabled) color = 0xFFFFFF
        drawCenteredString(
            mc.fontRenderer,
            if (feature.enabled) "§aEnabled" else "§cDisabled",
            this.x + 50,
            y + 6,
            color
        )
        drawRect(x, y, x + width, y + 1, -0x1)
        drawRect(x + width - 1, y, x + width, y + 20, -0x1)
        drawRect(x, y, x + 1, y + 20, -0x1)
        drawRect(x, y + 19, x + width, y + 20, -0x1)
    }
}
