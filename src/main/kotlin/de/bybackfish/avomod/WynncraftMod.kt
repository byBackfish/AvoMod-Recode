package de.bybackfish.avomod

import de.bybackfish.avomod.commands.dev.GetItemNBTCommand
import de.bybackfish.avomod.commands.dev.SetSettingCommand
import de.bybackfish.avomod.commands.dev.TestCommand
import de.bybackfish.avomod.commands.dev.ToggleCommand
import de.bybackfish.avomod.commands.util.OnlineMembers
import de.bybackfish.avomod.config.PersistantConfig
import de.bybackfish.avomod.features.FeatureHandler
import de.bybackfish.avomod.features.impl.test.TestFeature
import de.bybackfish.avomod.util.DataContainer
import de.bybackfish.wynnapi.WynnStats
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod(
    modid = WynncraftMod.MOD_ID,
    name = WynncraftMod.MOD_NAME,
    version = WynncraftMod.VERSION,
    modLanguageAdapter = "adapter.Adapter"
)
object WynncraftMod {
    const val MOD_ID = "wynncraftmod"
    const val MOD_NAME = "Wynncraft Mod"
    const val VERSION = "2.0.0"

    val featureHandler = FeatureHandler()
    val wynnstats: WynnStats = WynnStats()
    var guiToDraw: GuiScreen? = null
    var data: DataContainer = DataContainer()
    var inLocations = false;

    @Mod.EventHandler
    fun preinit(event: FMLPreInitializationEvent) {
        print("[AvoMod] Loaded")

        featureHandler.addFeature(TestFeature())

        GetItemNBTCommand()
        ToggleCommand()
        SetSettingCommand()
        TestCommand()
        OnlineMembers();

        val config = PersistantConfig("avomod.json")
        config.load()

        Runtime.getRuntime().addShutdownHook(Thread(config::save))

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(de.bybackfish.avomod.listener.GuiScreenEvent())
    }

    @SubscribeEvent
    fun onRender(_event: TickEvent.RenderTickEvent) {
        if (guiToDraw != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToDraw)
            guiToDraw = null
        }
    }

    @SubscribeEvent
    fun onRenderChat(_event: RenderGameOverlayEvent.Chat) {
        if (inLocations) return;
        featureHandler.getFeatures().filter {
            it.value.enabled
        }.forEach { feature ->
            if (feature.value.feature.isDrawing)
                feature.value.instance.render(feature.value.locationData.x, feature.value.locationData.y)
        }
    }
}

