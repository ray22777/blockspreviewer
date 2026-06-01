package net.tricube.blocks_previewer.platform.neoforge;

//? neoforge {

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.tricube.blocks_previewer.ModInit;


@EventBusSubscriber(modid = ModInit.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ModInit.onInitializeClient();
	}

}
//?}
