package de.bybackfish.avomod.commands.dev

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.commands.base.Kommand
import de.bybackfish.avomod.extensions.toComponent
import net.minecraft.client.Minecraft

class SetSettingCommand : Kommand(k@{
    if (it.isEmpty() || it.size < 2) {
        sendUsage()
        return@k
    }

    val featureName = it[0]
    val feature = WynncraftMod.featureHandler.getFeature(featureName)
    if (feature == null) {
        send("§cFeature not found!")
        return@k
    }

    val setting = feature.getSetting(it[1])
    if (setting == null) {
        send("§cSetting not found!")
        return@k
    }
    val value = it.drop(2).joinToString(" ")
    val prev = setting.getValue()
    try {
        setting.setValue(setting.setting.type.transformer(value))
        send("§aChanged §8${it[0]}/${it[1]}§a §7${prev} §8-> §7${value}")
    } catch (e: Exception) {
        send("§7Error setting the Setting. Make sure to use the right value type!")
    }
}, "setsetting",
    autoComplete = a@{ args ->
        if (args.isEmpty()) return@a mutableListOf<String>()
        val featureName = args[0]
        if (args.size == 1) {
            val features = WynncraftMod.featureHandler.getFeatures()
            return@a features.filter {
                it.value.feature.name.startsWith(featureName)
            }.map { it.value.feature.name }.toMutableList()
        } else if (args.size == 2) {
            val feature = WynncraftMod.featureHandler.getFeature(args[0])
            if (feature == null) {
                Minecraft.getMinecraft().player.sendMessage("§cFeature not found!".toComponent())
                return@a mutableListOf<String>()
            }
            val res = feature.getSettings().filter {
                it.value.field.name.startsWith(args[1])
            }.map { it.value.field.name }.toMutableList()
            println(res.size)
            return@a res
        }
        return@a mutableListOf<String>()
    })

