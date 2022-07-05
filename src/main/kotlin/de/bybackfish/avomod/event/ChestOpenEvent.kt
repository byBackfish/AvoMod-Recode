package de.bybackfish.avomod.event

import net.minecraft.client.gui.Gui
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.fml.common.eventhandler.Event

class ChestOpenEvent(val container: ContainerChest, val gui: Gui) : Event()
