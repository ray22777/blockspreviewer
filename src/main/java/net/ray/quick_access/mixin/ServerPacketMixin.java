package net.ray.quick_access.mixin;

import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
//?if>=26.1
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.*;
import net.ray.quick_access.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPacketMixin {

	@Shadow public ServerPlayer player;

	@Inject(method = "handleContainerClick", at = @At("HEAD"), cancellable = true)
	private void onContainerClick(ServerboundContainerClickPacket packet, CallbackInfo ci) {
		//?if>=26.1{
				if (packet.containerInput() != ContainerInput.PICKUP) return;
		//?}else if >=1.21.11{
		/*if (packet.clickType() != ClickType.PICKUP) return;
		*///?}else{
		//if(packet.getClickType() != ClickType.PICKUP) return;
		//?}
		//~ if >=1.21.11'.getButtonNum()' -> '.buttonNum()'
		if (packet.buttonNum() != 1) return;
		if (!player.containerMenu.getCarried().isEmpty()) return;
		//~ if >=1.21.11'.getSlotNum()' -> '.slotNum()'
		int slot = packet.slotNum();
		if (slot < 0) return;
		Slot menuslot = player.containerMenu.getSlot(slot);
		ItemStack stack = menuslot.getItem();
		if (!EventHandler.canOpen(stack)) return;

		if(EventHandler.isCrafting(player.containerMenu)) player.containerMenu.removed(player);//ensure items are returned

		if(player.containerMenu instanceof InventoryMenu){
			player.inventoryMenu.setCarried(ItemStack.EMPTY);
			player.inventoryMenu.resumeRemoteUpdates();
			player.inventoryMenu.broadcastFullState();
		}else{
			player.containerMenu.setCarried(ItemStack.EMPTY);
			player.containerMenu.resumeRemoteUpdates();
			player.containerMenu.broadcastFullState();
			player.containerMenu = player.inventoryMenu; //hack to prevent cursor from jumping to center
		}
		EventHandler.tryOpen(player,stack,menuslot,false);

		ci.cancel();

	}


}
