package de.bybackfish.avomod.features.impl.test

import de.bybackfish.avomod.annotation.*
import de.bybackfish.avomod.features.FeatureInherit
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


@Feature(
    name = "testFeature",
    displayName = "Testing Feature",
    description = "This is a test feature",
)
class TestFeature : FeatureInherit() {


    @Setting(
        displayName = "Test Setting",
        type = SettingType.STRING,
    )
    val value = "test";

    @OnInit
    fun init() {
        println("[testft] feature initialized")
    }

    @OnEnable
    fun enable() {
        println("[testft] feature enabled")
    }

    @OnDisable
    fun disable() {
        println("[testft] feature disabled")
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        println("[testft] tick with value: $value")
    }

}
