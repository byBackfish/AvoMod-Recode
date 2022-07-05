package de.bybackfish.avomod.gui.elements

import de.bybackfish.avomod.features.FeatureData
import de.bybackfish.avomod.gui.ConfigGui
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton


class CategoryElement(
    val feature: FeatureData,
    val index: Int,
    val configGui: ConfigGui,
    buttonId: Int,
    x: Int,
    y: Int,
    var title: String
) :
    GuiButton(buttonId, x, y, 100, 20, title) {
    private val x: Int = 0
    private val y: Int = 0

    init {
        this.x = x
        this.y = y
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int) {
        configGui.navigate(index)
    }


    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val enabled = configGui.currentFeature == index
        if (enabled) {
            val color = -0x1
            drawRect(x, y, x + 100, y + 1, color)
            drawRect(x + 99, y, x + 100, y + 20, color)
            drawRect(x, y, x + 1, y + 20, color)
            drawRect(x, y + 19, x + 100, y + 20, color)
        }
        var color = 0xBBBBBB
        if (enabled) color = 0xFFFFFF
        drawCenteredString(mc.fontRenderer, title, x + 50, y + 6, color)
    }

}
