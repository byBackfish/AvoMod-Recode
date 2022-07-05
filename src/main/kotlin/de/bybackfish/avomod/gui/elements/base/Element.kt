package de.bybackfish.avomod.gui.elements.base

import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.ConfigGui

interface Element {

    fun changed(settingData: SettingData, value: Any): Boolean {
        if (value.toString() === "") return true
        if (value.toString().isBlank() || value.toString().isEmpty()) return true
        return try {
            settingData.setValue(settingData.setting.type.transformer(value.toString()))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun onInitialize(configGui: ConfigGui) {

    }

    fun draw() {
    }

    fun isButton(): Boolean {
        return false
    }

}
