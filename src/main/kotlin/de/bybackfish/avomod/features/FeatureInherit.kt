package de.bybackfish.avomod.features

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.world.World

abstract class FeatureInherit {

    val player: EntityPlayerSP?
        get() = minecraft?.player
    val minecraft: Minecraft?
        get() = Minecraft.getMinecraft()
    val world: World?
        get() = minecraft?.world


    open fun render(x: Number, y: Number) {

    }

    open fun renderDummy(x: Number, y: Number) {
        render(x, y)
    }

    open fun getWidth(): Int {
        return 0
    }

    open fun getHeight(): Int {
        return 0
    }


}
