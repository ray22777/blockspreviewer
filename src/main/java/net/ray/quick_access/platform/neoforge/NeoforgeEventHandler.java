package net.ray.quick_access.platform.neoforge;

//? neoforge {

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.ray.quick_access.EventHandler;
import net.ray.quick_access.ModInit;

import static net.ray.quick_access.ModInit.MOD_ID;


@EventBusSubscriber(modid = MOD_ID)
public class NeoforgeEventHandler {

	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
		EventHandler.onRightClick(event.getEntity(), event.getLevel(), event.getHand());
	}
}
//?}
