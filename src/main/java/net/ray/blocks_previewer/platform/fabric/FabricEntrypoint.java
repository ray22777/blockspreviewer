package net.ray.blocks_previewer.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.ray.blocks_previewer.ModInit;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModInit.onInitialize();
//		FabricEventSubscriber.registerEvents();
	}
}
//?}
