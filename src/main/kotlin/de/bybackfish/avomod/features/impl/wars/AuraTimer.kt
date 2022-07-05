package de.bybackfish.avomod.features.impl.wars

import de.bybackfish.avomod.annotation.Feature
import de.bybackfish.avomod.annotation.Setting
import de.bybackfish.avomod.annotation.SettingType
import de.bybackfish.avomod.features.FeatureInherit
import de.bybackfish.avomod.util.RenderUtil
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.awt.Color
import java.util.*
import kotlin.math.floor

@Feature(
    name = "auraTimer",
    displayName = "Aura Timer",
    description = "Shows the remaining time of the aura",
    default = true
)
class AuraTimer : FeatureInherit() {

    @Setting(
        type = SettingType.STRING,
        displayName = "Color",
    )
    val colorName: String = "WHITE"

    private val auraProcTime = 3200
    private val potentialAuraTimes = intArrayOf(24, 18, 12)

    var auraTimer = 0
    var firstAura: Long = 0
    private var lastAura: Long = 0

    @SubscribeEvent
    fun onRender(event: TickEvent.RenderTickEvent) {
        kotlin.runCatching {
            val subTitle = ReflectionHelper.findField(GuiIngame::class.java, "displayedSubTitle", "field_175200_y")
                .get(minecraft!!.ingameGUI) as String
            if (subTitle.contains("Aura"))
                onAuraPing()
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderOverlay(event: RenderGameOverlayEvent.Chat) {
        draw()
    }

    private fun onAuraPing() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastAura > auraProcTime) {
            if (firstAura != 0L && auraTimer == 0) {
                auraTimer =
                    (System.currentTimeMillis() - firstAura).toInt() / 1000
                val differences: IntArray = Arrays.stream(potentialAuraTimes).map { e: Int -> e - auraTimer }.toArray()
                var lowestValue = 99999
                var lowestIndex = 0
                for (i in differences.indices) {
                    if (differences[i] < lowestValue) {
                        lowestIndex = i
                        lowestValue = differences[i]
                    }
                }
                auraTimer =
                    potentialAuraTimes[lowestIndex]
                println(auraTimer)
            }
            firstAura = currentTime
        }

        lastAura = currentTime
    }

    private fun draw() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - firstAura < auraProcTime) {
            val timeRemaining: Long =
                auraProcTime - (currentTime - firstAura)
            val remainingTimer = (floor(timeRemaining / 100.0) / 10.0).toString()
            GlStateManager.pushMatrix()
            GlStateManager.scale(6.0f, 6.0f, 6.0f)
            val scaledResolution = ScaledResolution(minecraft)
            val color: Color = try {
                Color.getColor(colorName)
            } catch (e: Exception) {
                Color(255, 111, 0)
            }

            RenderUtil.drawCenteredShadowedString(
                remainingTimer,
                scaledResolution.scaledWidth / 12,
                scaledResolution.scaledHeight / 12 - 3,
                color
            )
            if (currentTime - firstAura < 400) {
                RenderUtil.drawRect(
                    color,
                    0F,
                    0F,
                    scaledResolution.scaledWidth.toFloat(),
                    scaledResolution.scaledHeight.toFloat()
                )
            }
            GlStateManager.popMatrix()
        }
    }

}
