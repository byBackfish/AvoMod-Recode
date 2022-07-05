package de.bybackfish.avomod.listener

import de.bybackfish.avomod.event.ChestOpenEvent
import net.minecraft.client.Minecraft
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class GuiScreenEvent {

    @SubscribeEvent
    fun onRender(
        event: GuiScreenEvent.InitGuiEvent
    ) {
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().player?.openContainer == null) return
        val container = Minecraft.getMinecraft().player.openContainer
        if (container is ContainerChest)
            MinecraftForge.EVENT_BUS.post(
                ChestOpenEvent(
                    Minecraft.getMinecraft().player.openContainer as ContainerChest, event.gui
                )
            )
    }

}
