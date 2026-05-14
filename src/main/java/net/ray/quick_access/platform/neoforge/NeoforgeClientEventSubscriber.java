package net.ray.quick_access.platform.neoforge;

//? neoforge {

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.ray.quick_access.ModInit;

@EventBusSubscriber(modid = ModInit.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ModInit.onInitializeClient();
	}
}
//?}
