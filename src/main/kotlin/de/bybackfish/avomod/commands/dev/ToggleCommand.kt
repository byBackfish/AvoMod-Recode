package de.bybackfish.avomod.commands.dev

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.commands.base.Kommand

class ToggleCommand : Kommand(k@{
    if (it.isEmpty()) {
        sendUsage()
        return@k
    }

    val featureName = it[0]
    val feature = WynncraftMod.featureHandler.getFeature(featureName)
    if (feature == null) {
        send("§cFeature not found!")
        return@k
    }

    feature.toggle()
    send("§aToggled feature §e${feature.feature.name}§a! §7(§e${feature.enabled}§7)")
}, "togglefeature",

    autoComplete = a@{ args ->
        if (args.isEmpty()) return@a mutableListOf<String>()
        val featureName = args[0]
        val features = WynncraftMod.featureHandler.getFeatures()
        return@a features.filter {
            it.value.feature.name.startsWith(featureName)
        }.map { it.value.feature.name }.toMutableList()
    })

