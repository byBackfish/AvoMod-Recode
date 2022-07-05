package de.bybackfish.avomod.gui

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.extensions.toComponent
import de.bybackfish.avomod.features.FeatureData
import de.bybackfish.avomod.util.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color
import javax.annotation.Nonnull


class LocationsGUI : GuiScreen() {

    var selectedFeature: FeatureData? = null;
    val lastTickPosX = hashMapOf<String, Int>()
    val lastTickPosY = hashMapOf<String, Int>()

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawWorldBackground(0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, "Avomod Locations", this.width / 4 + 1, 6, 0x444444);
        this.drawCenteredString(this.fontRenderer, "Avomod Locations", this.width / 4, 5, 0x1B33CF);
        GlStateManager.popMatrix();

        val features = WynncraftMod.featureHandler.getFeatures().filter { it.value.enabled }
        features.forEach { (name, feature) ->
            if (feature.feature.isDrawing) {


                if (lastTickPosX[feature.feature.name] == null) {
                    lastTickPosX[feature.feature.name] = feature.locationData.x
                }
                if (lastTickPosY[feature.feature.name] == null) {
                    lastTickPosY[feature.feature.name] = feature.locationData.y
                }

                val actualPosx =
                    lastTickPosX[feature.feature.name]!! + ((feature.locationData.x - lastTickPosX[feature.feature.name]!!) * partialTicks)
                val actualPosy =
                    lastTickPosY[feature.feature.name]!! + (feature.locationData.y - lastTickPosY[feature.feature.name]!!) * partialTicks
                feature.instance.renderDummy(actualPosx, actualPosy);
                //draw rect around it
                RenderUtil.drawHollowRect(
                    Color.WHITE,
                    actualPosx.toInt() - 3,
                    actualPosy.toInt() - 3,
                    3 + feature.instance.getWidth(),
                    3 + feature.instance.getHeight()
                );
                lastTickPosX[feature.feature.name] = feature.locationData.x
                lastTickPosY[feature.feature.name] = feature.locationData.y
            }
        };

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    override fun onResize(@Nonnull mineIn: Minecraft, w: Int, h: Int) {
        super.onResize(mineIn, w, h)
        initGui()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        //check if any of the elements was clicked
        selectedFeature = null
        val features = WynncraftMod.featureHandler.getFeatures().filter { it.value.enabled }
        features.forEach { (name, feature) ->
            if (!feature.feature.isDrawing) return
            val x = feature.locationData.x;
            val y = feature.locationData.y

            if (mouseX >= x - feature.instance.getWidth() && mouseX <= x + feature.instance.getWidth() && mouseY >= y - feature.instance.getHeight() && mouseY <= y + feature.instance.getHeight()) {
                selectedFeature = feature
                Minecraft.getMinecraft().player.sendMessage(("Selected: " + feature.feature.name).toComponent())
                return@forEach;
            } else {
                Minecraft.getMinecraft().player.sendMessage(("Not: " + feature.feature.name).toComponent())
            }
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        if (selectedFeature != null) {
            selectedFeature!!.locationData.x = mouseX - selectedFeature!!.instance.getWidth() / 2
            selectedFeature!!.locationData.y = mouseY - selectedFeature!!.instance.getHeight() / 2
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (selectedFeature != null) {
            selectedFeature!!.locationData.x = mouseX - selectedFeature!!.instance.getWidth() / 2
            selectedFeature!!.locationData.y = mouseY - selectedFeature!!.instance.getHeight() / 2
            selectedFeature = null
        }
    }

    override fun initGui() {
        WynncraftMod.inLocations = true
    }

    override fun onGuiClosed() {
        WynncraftMod.inLocations = false
    }

}
