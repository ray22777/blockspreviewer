package net.ray.better_tab.platform.neoforge;

//? neoforge {

/*import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.ray.better_tab.SinglePlayerTab;

import static net.ray.better_tab.ModInit.MOD_ID;


@EventBusSubscriber(modid = MOD_ID)
public class NeoforgeEventHandler {

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiLayersEvent event) {
		event.registerAboveAll(
				ResourceLocation.fromNamespaceAndPath("craftconfig", "singleplayer_tab"),
				(guiGraphics, deltaTracker) -> SinglePlayerTab.renderTab(guiGraphics)
		);
	}
}
*///?}
