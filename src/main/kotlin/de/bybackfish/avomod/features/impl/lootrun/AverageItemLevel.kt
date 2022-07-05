package de.bybackfish.avomod.features.impl.lootrun

import com.mojang.realmsclient.gui.ChatFormatting
import de.bybackfish.avomod.annotation.Feature
import de.bybackfish.avomod.annotation.Setting
import de.bybackfish.avomod.annotation.SettingType
import de.bybackfish.avomod.event.ChestOpenEvent
import de.bybackfish.avomod.features.FeatureInherit
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.InventoryBasic
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.util.*


@Feature(
    name = "averageItemLevel",
    description = "Calculates the average item level of the Chest",
    default = false,
    displayName = "Average Item Level"
)
class AverageItemLevel : FeatureInherit() {
    var currentAverage = "0"

    @Setting(
        displayName = "Color",
        type = SettingType.STRING,
    )
    val color = "WHITE";

    @Setting(
        displayName = "Prefix",
        type = SettingType.STRING,
    )
    val prefix = "Average Item Level: ";

    @SubscribeEvent
    fun onOpen(event: ChestOpenEvent) {
        println("Opened chests")
        val chest = event.container.lowerChestInventory
        val chestName = chest.name
        if (!chestName.startsWith("Loot Chest")) return
        println("Opened Loot chest")

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val container = Minecraft.getMinecraft().player.openContainer
                if (container != null) {
                    var totalItems = 0
                    var totalLevel = 0
                    for (i in 0..26) {
                        val item = container.getSlot(i).stack
                        if (item.isEmpty) {
                            continue
                        }
                        val name = TextFormatting.getTextWithoutFormattingCodes(item.displayName)!!
                        if (name.startsWith("Unidentified ")) {
                            val tooltip =
                                item.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.ADVANCED)
                            tooltip.forEach {
                                val line = TextFormatting.getTextWithoutFormattingCodes(it)
                                if (line != null) {
                                    if (line.contains("Lv. Range: ")) {
                                        val levelRange = line.split(": ")[1]
                                        val levelAvg =
                                            (levelRange.split("-")[0].toInt() + levelRange.split("-")[1].toInt()) / 2
                                        println("$line | $levelAvg ($totalItems | $totalLevel)")
                                        totalItems++
                                        totalLevel += levelAvg
                                    }
                                }
                            }
                        }
                        if (totalItems == 0) {
                            currentAverage = "No items found";
                        } else {
                            currentAverage = (totalLevel / totalItems).toString()
                        }
                    }
                }
            }
        }, 100)
    }

    @SubscribeEvent
    fun onRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (event.gui == null) return
        if (player == null) return;
        val openContainer = player?.openContainer
        if (openContainer is ContainerChest) {
            val lowerInventory = (openContainer.lowerChestInventory as InventoryBasic)
            val containerName = ChatFormatting.stripFormatting(lowerInventory.name)
            if (containerName.startsWith("Loot Chest")) {

                val fontRenderer: FontRenderer = minecraft!!.fontRenderer
                val displayString = "$prefix $currentAverage"
                val screenWidth = event.gui.width
                val screenHeight = event.gui.height
                val c = try {
                    Color.getColor(color)
                } catch (e: Exception) {
                    Color.WHITE
                }
                fontRenderer.drawString(
                    displayString,
                    screenWidth / 2 - fontRenderer.getStringWidth(displayString) / 2,
                    screenHeight / 2 - 100,
                    0xFFFFFF
                )
            }
        }
    }
}
