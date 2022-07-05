package de.bybackfish.avomod.features

import de.bybackfish.avomod.annotation.*
import net.minecraftforge.common.MinecraftForge
import java.lang.reflect.Field
import kotlin.reflect.KClass

class FeatureHandler {

    private val features = hashMapOf<KClass<out FeatureInherit>, FeatureData>()

    fun addFeature(feature: FeatureInherit) {
        //check if feature is annotated with @Feature
        val annotation = feature.javaClass.isAnnotationPresent(Feature::class.java)

        if (annotation) {
            val data = feature.javaClass.getAnnotation(Feature::class.java)
            val featureData = FeatureData(
                feature = data,
                instance = feature,
                enabled = data.default
            )
            features[feature::class] = featureData
        } else {
            throw IllegalArgumentException("Feature must be annotated with @Feature")
        }
    }

    fun addFeaturez(vararg feature: FeatureInherit) {
        for (f in feature) {
            addFeature(f)
        }
    }

    fun test() {
        features.forEach { (key, it) ->
            println("[AVO]" + it.feature.name)
        }
    }

    fun getFeature(feature: KClass<*>): FeatureData? {
        return features[feature]
    }

    fun getFeature(feature: String): FeatureData? {
        return features.values.find { it.feature.name == feature }
    }

    fun getFeatures(): HashMap<KClass<out FeatureInherit>, FeatureData> {
        return features
    }

}

data class FeatureData(
    val feature: Feature,
    val instance: FeatureInherit,
    var enabled: Boolean,
    var locationData: LocationData = LocationData(100, 100)
) {

    private val settings = hashMapOf<String, SettingData>()

    init {
        runMethod(OnInit::class)
        getFields().forEach {
            println("[AVO] Found Setting: ${it.name}")
            it.isAccessible = true
            val data = it.getAnnotation(Setting::class.java)
            val featureData = SettingData(
                feature = this,
                field = it,
                setting = data
            )
            settings[it.name] = featureData
        }
    }

    private fun runMethod(anno: KClass<out Annotation>) {
        instance.javaClass.methods.filter { it.isAnnotationPresent(anno.java) }.forEach {
            it.invoke(instance)
        }
    }

    fun getSetting(setting: String): SettingData? {
        return settings[setting]
    }

    private fun getField(setting: String): Field? {
        val field = instance.javaClass.getDeclaredField(setting)
        field.isAccessible = true
        if (field.isAnnotationPresent(Setting::class.java))
            return field
        return null
    }

    fun getSettings(): HashMap<String, SettingData> {
        return settings
    }

    private fun getFields(): List<Field> {
        println("[AVO] Getting Fields")
        return this.instance.javaClass.declaredFields.filter {
            println("[AVO] Found Setting: ${it.name}")
            it.isAnnotationPresent(Setting::class.java)
        }
    }

    fun toggle(): Boolean {
        enabled = !enabled

        if (enabled) {
            MinecraftForge.EVENT_BUS.register(this.instance)
            runMethod(OnEnable::class)
        } else {
            MinecraftForge.EVENT_BUS.unregister(this.instance)
            runMethod(OnDisable::class)
        }

        return enabled
    }
}

data class SettingData(val feature: FeatureData, val setting: Setting, val field: Field, val hidden: Boolean = false) {

    fun setValue(v: Any) {
        field.set(this.feature.instance, v)
    }

    fun getValue(): Any? {
        return field.get(this.feature.instance)
    }

}

data class LocationData(var x: Int = 50, var y: Int = 50)
