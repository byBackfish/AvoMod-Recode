package de.bybackfish.avomod.gui

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.features.FeatureData
import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.elements.CategoryElement
import de.bybackfish.avomod.gui.elements.FeatureToggleButton
import de.bybackfish.avomod.gui.elements.base.Element
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color
import java.io.IOException


class ConfigGui : GuiScreen() {

    private val settingLineHeight = 27
    private val startingHeight = 100
    private val settingHeight = 23
    private var init = false

    val features: List<FeatureData> = WynncraftMod.featureHandler.getFeatures().values.toList()
    val elements = hashMapOf<String, Element>()

    var currentFeature = 0

    private val selectedFeature: FeatureData
        get() = this.features[this.currentFeature]

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawWorldBackground(0)
        GlStateManager.pushMatrix()
        GlStateManager.scale(2.0f, 2.0f, 2.0f)
        drawCenteredString(fontRenderer, "Avomod Configs", width / 4 + 1, 14, 0x444444)
        drawCenteredString(fontRenderer, "Avomod Configs", width / 4, 13, 0x1B33CF)
        GlStateManager.popMatrix()
        drawVerticalLine(width / 16 + 110, startingHeight - 10, height - 10, Color(255, 255, 255))


        val selectedFeatureSettings = selectedFeature.getSettings().toList()
        for (setting in selectedFeatureSettings) {
            val y = selectedFeatureSettings.indexOf(setting)
            val color = 0xFFFFFF

            // Draws the act
            drawString(
                fontRenderer,
                setting.second.setting.displayName,
                width / 16 + 121,
                (y * settingLineHeight) + (y * settingHeight) + 6 + startingHeight,
                color
            )
            drawHorizontalLine(
                width / 16 + 118,
                width - width / 16 - 21,
                y * settingLineHeight + (y + 1) * settingHeight + 23 + startingHeight,
                Color(255, 255, 255)
            )

            elements[setting.second.setting.displayName]!!.draw()
        }


        drawString(fontRenderer, selectedFeature.feature.description, 85, 100, 0x808080);

        try {
            super.drawScreen(mouseX, mouseY, partialTicks)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun navigate(index: Int) {
        this.buttonList = ArrayList()
        this.currentFeature = index
        this.initGui()
    }


    override fun onResize(mcIn: Minecraft, w: Int, h: Int) {
        super.onResize(mcIn, w, h)
        this.initGui()
    }

    private fun registerSettingElement(setting: SettingData) {
        val id = elements.size

        val element: Element?
        if (setting.setting.type.button) {
            element = setting.setting.type.element(
                setting,
                id,
                this.width / 16 + 121,
                id * settingLineHeight + startingHeight - 4 + (settingHeight * (id + 1)),
                height,
                width
            )
        } else {
            element = setting.setting.type.element(
                setting,
                id,
                this.width / 16 + 122,
                id * settingLineHeight + startingHeight - 2 + (settingHeight * (id + 1)),
                160,
                16
            )
        }
        element.onInitialize(this)
        this.elements[setting.setting.displayName] = element
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    fun getInputFields(): Map<String, SettingData> {
        return this.selectedFeature.getSettings().filter {
            it.value.setting.type.button
        }
    }

    private fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
        drawRectCustom(color, x.toFloat(), startY.toFloat(), 1F, endY.toFloat() - startY.toFloat())
    }

    private fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
        drawRectCustom(color, startX.toFloat(), y.toFloat(), endX.toFloat() - startX.toFloat(), 1F)
    }

    private fun drawRectCustom(color: Color, x: Float, y: Float, width: Float, height: Float) {
        val pos = floatArrayOf(x, y, x + width, y + height)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.buffer
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
        worldRenderer.begin(7, DefaultVertexFormats.POSITION)
        worldRenderer.pos(pos[0].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[1].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[0].toDouble(), pos[1].toDouble(), 0.0).endVertex()
        tessellator.draw()
        // set it back to normal
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun addButton(button: GuiButton) {
        this.buttonList.add(button)
    }


    override fun initGui() {
        this.buttonList = ArrayList()
        this.elements.clear()

        this.features.forEachIndexed { index, it ->
            if (selectedFeature == it) {
                for (setting in it.getSettings()) {
                    if (!setting.value.hidden)
                        registerSettingElement(setting.value)
                }
            }

            val configCategory = CategoryElement(
                it,
                index,
                this,
                100 + index,
                this.width / 16,
                this.startingHeight + index * settingLineHeight,
                it.feature.displayName

            )
            this.buttonList.add(configCategory)
        }

        if (!this.init) {
            super.initGui()
            this.init = true
        }

        this.buttonList.add(FeatureToggleButton(selectedFeature, width / 2 - 49, 45))
    }

    @Throws(IOException::class)
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        for (setting in selectedFeature.getSettings()) {
            val element = elements[setting.value.setting.displayName]
            if (element is GuiTextField)
                element.mouseClicked(mouseX, mouseY, mouseButton)
        }
    }

    @Throws(IOException::class)
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        for (setting in selectedFeature.getSettings()) {
            val element = elements[setting.value.setting.displayName]
            if (element is GuiTextField)
                element.textboxKeyTyped(typedChar, keyCode)
        }
    }
}
