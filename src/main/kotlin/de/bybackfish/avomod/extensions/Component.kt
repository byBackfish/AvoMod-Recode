package de.bybackfish.avomod.extensions

import net.minecraft.util.text.TextComponentString

fun Any.toComponent(): TextComponentString {
    return TextComponentString(this.toString())
}
