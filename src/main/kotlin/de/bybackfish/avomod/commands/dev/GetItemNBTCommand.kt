package de.bybackfish.avomod.commands.dev

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.commands.base.Kommand
import de.bybackfish.avomod.gui.LocationsGUI
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


class GetItemNBTCommand : Kommand({


    WynncraftMod.guiToDraw = LocationsGUI();

    val item = player.inventory.getCurrentItem()
    val string = item.serializeNBT().toString()
    println(item.serializeNBT())
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(string), null)

}, "getnbtofitem")
