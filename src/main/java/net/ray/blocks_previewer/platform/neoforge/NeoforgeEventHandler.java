package net.ray.blocks_previewer.platform.neoforge;

//? neoforge {

/*import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.ray.blocks_previewer.SinglePlayerTab;

import static net.ray.blocks_previewer.ModInit.MOD_ID;


@EventBusSubscriber(modid = MOD_ID)
public class NeoforgeEventHandler {

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiLayersEvent event) {
		event.registerAboveAll(
				Identifier.fromNamespaceAndPath("craftconfig", "singleplayer_tab"),
				(guiGraphics, deltaTracker) -> SinglePlayerTab.renderTab(guiGraphics)
		);
	}
}
*///?}
