package de.bybackfish.avomod.gui.elements

import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.ConfigGui
import de.bybackfish.avomod.gui.elements.base.Element
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiTextField

class InputElement(
    private val setting: SettingData,
    componentId: Int,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : GuiTextField(
    componentId,
    Minecraft.getMinecraft().fontRenderer,
    x,
    y,
    width,
    height

), Element {

    override fun writeText(textToWrite: String) {
        super.writeText(textToWrite)
        changed(this.setting, this.text)
    }

    override fun draw() {
        this.drawTextBox()
    }

    override fun onInitialize(configGui: ConfigGui) {
        this.text = setting.getValue().toString()
    }
}
