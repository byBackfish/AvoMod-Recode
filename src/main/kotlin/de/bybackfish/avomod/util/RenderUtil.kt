package de.bybackfish.avomod.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color

object RenderUtil {

    fun drawRect(color: Color, x: Float, y: Float, width: Float, height: Float) {
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

    fun drawRect(color: Color, x: Int, y: Int, width: Int, height: Int) {
        drawRect(color, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    }

    fun drawHollowRect(color: Color, x: Int, y: Int, width: Int, height: Int) {
        drawHorizontalLine(x, x + width, y, color)
        drawHorizontalLine(x, x + width, y + height, color)
        drawVerticalLine(x, y, y + height, color)
        drawVerticalLine(x + width, y, y + height, color)
    }

    fun drawString(text: String?, x: Int, y: Int, color: Color) {
        val fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRenderer
        fontRenderer.drawString(text, x, y, color.rgb)
    }

    fun drawStringWithShadow(text: String?, x: Int, y: Int, color: Color) {
        val fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRenderer
        fontRenderer.drawString(text, x + 1, y + 1, getContrastColor(color).rgb)
        fontRenderer.drawString(text, x, y, color.rgb)
    }

    fun drawCenteredShadowedString(text: String?, x: Int, y: Int, color: Color) {
        val fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRenderer
        fontRenderer.drawString(
            text,
            x - fontRenderer.getStringWidth(text) / 2 + 1,
            y + 1,
            getContrastColor(color).rgb
        )
        fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color.rgb)
    }

    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
        drawRect(color, startX.toFloat(), y.toFloat(), (endX - startX).toFloat(), 1f)
    }

    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
        drawRect(color, x.toFloat(), startY.toFloat(), 1f, (endY - startY).toFloat())
    }

    fun getStringWidth(text: String?): Int {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text)
    }

    fun getContrastColor(color: Color): Color {
        val y = (299.0 * color.red + 587.0 * color.green + 114.0 * color.blue) / 1000
        return if (y >= 128) Color.black else Color.white
    }
}
