package de.bybackfish.avomod.config

import com.google.gson.*
import de.bybackfish.avomod.WynncraftMod
import java.io.File

class PersistantConfig(path: String) {

    var file: File
    var parser: JsonParser = JsonParser()
    var config: JsonObject

    var gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()


    init {
        println("init PersistantConfig")
        file = File(path)
        if (!file.exists()) {
            println("file not found")
            file.createNewFile()

            config = JsonObject()
            loadDefaults()
        }
        config = parser.parse(file.readText()).asJsonObject
    }

    private fun loadDefaults() {
        config.addProperty("version", WynncraftMod.VERSION)
        val json = JsonObject()

        for (feature in WynncraftMod.featureHandler.getFeatures()) {
            val featureJson = JsonObject()
            featureJson.addProperty("enabled", feature.value.enabled)
            val settings = JsonArray()
            for (setting in feature.value.getSettings()) {
                settings.add(JsonObject().also {
                    it.addProperty("name", setting.key)
                    it.addProperty("value", setting.value.getValue().toString())
                })
            }
            if (feature.value.feature.isDrawing)
                featureJson.add("locations", JsonObject().also {
                    it.addProperty("x", feature.value.locationData.x)
                    it.addProperty("y", feature.value.locationData.y)
                })
            featureJson.add("settings", settings)
            json.add(feature.value.feature.name, featureJson)
        }

        config.add("features", json)

        save(false)
    }

    fun save(refresh: Boolean = true) {
        if (refresh)
            refresh()
        file.writeText(config.toString())
    }

    private fun refresh() {
        val featureDatas = config.getAsJsonObject("features")
        for ((clazz, feature) in WynncraftMod.featureHandler.getFeatures()) {
            val data = featureDatas.get(feature.feature.name).asJsonObject
            data.addProperty("enabled", feature.enabled)
            val settingsArray = JsonArray()
            for ((name, setting) in feature.getSettings()) {
                settingsArray.add(
                    JsonObject().also {
                        it.addProperty("name", name)
                        it.addProperty("value", setting.getValue().toString())
                    }
                )
            }
            if (feature.feature.isDrawing)
                data.add(
                    "locations",
                    JsonObject().also {
                        it.addProperty("x", feature.locationData.x); it.addProperty(
                        "y",
                        feature.locationData.y
                    )
                    })
            data.add("settings", settingsArray)
            featureDatas.add(feature.feature.name, data)
        }
        config.add("features", featureDatas)

    }

    fun load() {
        val featureHandler = WynncraftMod.featureHandler
        val features = config.getAsJsonObject("features")
        for ((clas, feature) in featureHandler.getFeatures()) {
            if (features.has(feature.feature.name)) {
                val featureData = features.getAsJsonObject(feature.feature.name)
                println("[AVO] Feature ${feature.feature.name} found in config | ${featureData.get("enabled").asBoolean}")
                if (featureData.get("enabled").asBoolean)
                    feature.toggle()

                if (feature.feature.isDrawing) {
                    val location = featureData.getAsJsonObject("locations");
                    feature.locationData.x = location.get("x").asInt
                    feature.locationData.y = location.get("y").asInt
                }
                val settingsArray = featureData.getAsJsonArray("settings")
                for (settingObj in settingsArray) {
                    val obj = settingObj.asJsonObject
                    if (feature.getSetting(obj.get("name").asString) != null) {
                        val setting = feature.getSetting(obj.get("name").asString)!!
                        setting.setValue(setting.setting.type.transformer(obj.get("value").asString))
                    }
                }
                println("[AVO] Found Config for ${feature.feature.name}")
            } else {
                val featureData = JsonObject()
                featureData.addProperty("enabled", feature.enabled)
                val settingsArray = JsonArray()
                for (setting in feature.getSettings()) {
                    settingsArray.add(
                        JsonObject().also {
                            it.addProperty("name", setting.key)
                            it.addProperty("value", setting.value.getValue().toString())
                        })
                }
                featureData.add(
                    "locations",
                    JsonObject().also {
                        it.addProperty("x", feature.feature.defaultX); it.addProperty(
                        "y",
                        feature.feature.defaultY
                    )
                    })
                featureData.add("settings", settingsArray)
                config.getAsJsonObject("features").add(feature.feature.name, featureData)
                println("[AVO] Created Config for ${feature.feature.name}")
                save()
            }
        }
    }

    infix fun Number.floor(other: Number): Number {
        return kotlin.math.floor(this.toDouble() / other.toDouble())
    }

    fun a() {

    }

}
