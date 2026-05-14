package net.ray.quick_access.platform.forge;

//? forge {
/*
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ray.quick_access.EventHandler;

import static net.ray.quick_access.ModInit.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
	@SubscribeEvent
	public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
		EventHandler.onRightClick(event.getEntity(), event.getLevel(), event.getHand());
	}
}
*///?}
