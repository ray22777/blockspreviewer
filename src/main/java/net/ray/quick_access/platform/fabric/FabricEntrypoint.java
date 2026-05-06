package net.ray.quick_access.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.ray.quick_access.EventHandler;
import net.ray.quick_access.ModInit;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModInit.onInitialize();
		UseItemCallback.EVENT.register(EventHandler::onRightClick);
//		FabricEventSubscriber.registerEvents();
	}
}
//?}
