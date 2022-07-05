package de.bybackfish.avomod.commands.dev

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.commands.base.Kommand
import de.bybackfish.avomod.gui.ConfigGui

class TestCommand : Kommand({

    WynncraftMod.guiToDraw = ConfigGui()


}, "test")
