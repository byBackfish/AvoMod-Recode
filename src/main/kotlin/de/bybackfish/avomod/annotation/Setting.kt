package de.bybackfish.avomod.annotation

import de.bybackfish.avomod.features.SettingData
import de.bybackfish.avomod.gui.elements.BooleanElement
import de.bybackfish.avomod.gui.elements.InputElement
import de.bybackfish.avomod.gui.elements.IntegerElement
import de.bybackfish.avomod.gui.elements.base.Element

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Setting(
    val displayName: String,
    val type: SettingType,
    val max: Double = Double.MAX_VALUE,
    val min: Double = Double.MIN_VALUE
)

enum class SettingType(
    val button: Boolean,
    val transformer: (String) -> Any,
    val element: (setting: SettingData, id: Int, x: Int, y: Int, height: Int, width: Int) -> Element
) {
    STRING(false, {
        it
    }, { setting, id, x, y, height, width ->
        InputElement(setting, id, x, y, height, width)
    }),
    INT(false, {
        it.toInt()
    }, { setting, id, x, y, height, width ->
        IntegerElement(setting, id, x, y, height, width)
    }),
    DOUBLE(false, {
        it.toDouble()
    }, { setting, id, x, y, height, width ->
        IntegerElement(setting, id, x, y, height, width)
    }),
    OPTION(false, {
        it
    }, STRING.element),
    BOOLEAN(true, {
        it.toBoolean()
    }, { setting, id, x, y, _, _ ->
        BooleanElement(setting, id, x, y)
    })
}
