package de.bybackfish.avomod.gui.elements

import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.ConfigGui
import de.bybackfish.avomod.gui.elements.base.Element
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton


class BooleanElement
    (
    private val setting: SettingData,
    buttonId: Int,
    x: Int,
    y: Int,
    default: Boolean = false
) : GuiButton(
    buttonId,
    x,
    y,
    ""
), Element {

    var value = setting.getValue() as Boolean

    override fun mouseReleased(mouseX: Int, mouseY: Int) {
        this.value = !this.value
        this.displayString = if (this.value) "Enabled" else "Disabled"
        this.changed(this.setting, this.value)
    }

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawRect(x, y, x + width, y + 1, -0x1)
        drawRect(x + width - 1, y, x + width, y + 20, -0x1)
        drawRect(x, y, x + 1, y + 20, -0x1)
        drawRect(x, y + 19, x + width, y + 20, -0x1)
        var color = 0xFF8888
        if (this.value) color = 0x88FF88
        drawCenteredString(mc.fontRenderer, if (this.value) "Enabled" else "Disabled", x + width / 2, y + 6, color)
    }

    override fun onInitialize(configGui: ConfigGui) {
        configGui.addButton(this)
    }

    override fun isButton(): Boolean {
        return true
    }

}
