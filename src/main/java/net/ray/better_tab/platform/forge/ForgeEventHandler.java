package net.ray.better_tab.platform.forge;

//? forge {

/*import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ray.better_tab.SinglePlayerTab;

import static net.ray.better_tab.ModInit.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAboveAll(
				"singleplayer_tab",
				(forgeGui, guiGraphics, partialTick, screenWidth, screenHeight) -> SinglePlayerTab.renderTab(guiGraphics)
		);
    }
}
*///?}
