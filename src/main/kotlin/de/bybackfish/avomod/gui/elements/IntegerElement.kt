package de.bybackfish.avomod.gui.elements

import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.elements.base.Element
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiTextField

class IntegerElement(
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
    height,
), Element {

    override fun writeText(textToWrite: String) {
        //check if text is a number
        if (textToWrite.matches(Regex("[0-9]+"))) {
            if (textToWrite.toInt() <= setting.setting.max.toInt() && textToWrite.toInt() >= setting.setting.min.toInt()) {
                if (changed(this.setting, this.text))
                    super.writeText(textToWrite)
            }
        }
    }

    override fun draw() {
        this.drawTextBox()
    }
}
