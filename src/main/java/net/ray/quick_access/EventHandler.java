package net.ray.quick_access;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class EventHandler {
	public static boolean canOpen(ItemStack stack) {
		return
				(stack.is(Items.CRAFTING_TABLE) && Config.getBoolean("enableCraftingTable", true)) ||
				(stack.is(Items.ENDER_CHEST) && Config.getBoolean("enableEnderChest", true)) ||
				(stack.is(ItemTags.SHULKER_BOXES) && Config.getBoolean("enableShulkerBox", true)) ||
				(stack.is(Items.LOOM) && Config.getBoolean("enableLoom", true)) ||
				(stack.is(Items.CARTOGRAPHY_TABLE) && Config.getBoolean("enableCartographyTable", true)) ||
				(stack.is(Items.STONECUTTER) && Config.getBoolean("enableStonecutter", true)) ||
				(stack.is(Items.GRINDSTONE) && Config.getBoolean("enableGrindstone", true)) ||
				(stack.is(Items.SMITHING_TABLE) && Config.getBoolean("enableSmithingTable", true));
	}
	public static boolean isCrafting(AbstractContainerMenu menu) {
		return
				menu instanceof CraftingMenu ||
				menu instanceof LoomMenu||
				menu instanceof CartographyTableMenu ||
				menu instanceof StonecutterMenu||
				menu instanceof GrindstoneMenu ||
				menu instanceof SmithingMenu;
	}
	public static boolean tryOpen(ServerPlayer player, ItemStack stack, Slot slot,boolean isRightClick) {
		if (!canOpen(stack)) {
			return false;
		}
		ContainerLevelAccess access = ContainerLevelAccess.create(player.level(), player.blockPosition());
		if (stack.is(Items.CRAFTING_TABLE)) {
			open(player, Component.translatable("container.crafting"), (id, inv, p) ->
					new CraftingMenu(id, inv,access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		else if (stack.is(Items.ENDER_CHEST)) {
			if(Config.getBoolean("playEnderChestSound", true)){
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5f, 1.0f);
			}
			SimpleContainer ec = player.getEnderChestInventory();
			open(player, Component.translatable("container.enderchest"), (id, inv, p) ->
					new ChestMenu(MenuType.GENERIC_9x3, id, inv, ec, 3) {
						@Override
						public void removed(Player player) {
							if(Config.getBoolean("playEnderChestSound", true)){
								player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
										SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5f, 1.0f);
							}
							super.removed(player);
						}
					});
			return true;
		}
		else if (stack.is(ItemTags.SHULKER_BOXES)) {
			if(Config.getBoolean("playShulkerSound", true)){
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5f, 1.0f);
			}
			openShulker(player, stack,slot,isRightClick);
			return true;
		}
		else if (stack.is(Items.LOOM)) {
			open(player, Component.translatable("container.loom"), (id, inv, p) ->
					new LoomMenu(id, inv,access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		else if (stack.is(Items.CARTOGRAPHY_TABLE)) {
			open(player, Component.translatable("container.cartography_table"), (id, inv, p) ->
					new CartographyTableMenu(id, inv,access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		else if (stack.is(Items.STONECUTTER)) {
			open(player, Component.translatable("container.stonecutter"), (id, inv, p) ->
					new StonecutterMenu(id, inv,access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		else if (stack.is(Items.GRINDSTONE)) {
			open(player, Component.translatable("container.grindstone_title"), (id, inv, p) ->
					new GrindstoneMenu(id, inv, access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		else if (stack.is(Items.SMITHING_TABLE)) {
			open(player, Component.translatable("container.upgrade"), (id, inv, p) ->
					new SmithingMenu(id, inv, access) {
						@Override public boolean stillValid(Player p) { return true; }
					});
			return true;
		}
		return false;
	}

	public static void openShulker(ServerPlayer player, ItemStack shulkerStack, Slot slot,boolean isRightClick) {
		open(player, shulkerStack.getHoverName(), (id, inv, p) -> {
			SimpleContainer container = new SimpleContainer(ShulkerBoxBlockEntity.CONTAINER_SIZE) {
				@Override
				public void setChanged() {
					super.setChanged();
					saveToShulker(shulkerStack, slot, this);
				}
			};

			ItemContainerContents existing = shulkerStack.get(DataComponents.CONTAINER);
			if (existing != null) existing.copyInto(container.items);

			return new ShulkerBoxMenu(id, inv, container) {
				@Override
				public boolean stillValid(Player player) {
					ItemStack item;
					if(isRightClick){
						item = player.getItemInHand(InteractionHand.MAIN_HAND);
					}
					else{
						item = slot.getItem();
					}
					return !item.isEmpty() && ItemStack.isSameItemSameComponents(item, shulkerStack);
				}

				@Override
				public void removed(Player player) {
					saveToShulker(shulkerStack, slot, container);
					if(Config.getBoolean("playShulkerSound", true)){
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
								SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5f, 1.0f);
					}
					super.removed(player);
				}
			};
		});
	}

	private static void saveToShulker(ItemStack shulkerStack, Slot slot, SimpleContainer container) {
		if (shulkerStack.isEmpty() || !shulkerStack.is(ItemTags.SHULKER_BOXES)) {
			return;
		}

		boolean hasItems = false;
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (!container.getItem(i).isEmpty()) {
				hasItems = true;
				break;
			}
		}

		if (hasItems) {
			shulkerStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(container.items));
		} else {
			shulkerStack.remove(DataComponents.CONTAINER);
		}
	}

	public static void open(ServerPlayer player, Component title, MenuConstructor constructor) {
		player.openMenu(new MenuProvider() {
			@Override public Component getDisplayName() { return title; }
			@Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
				return constructor.create(id, inv, p);
			}
		});
	}

	@FunctionalInterface
	public interface MenuConstructor {
		AbstractContainerMenu create(int id, Inventory inv, Player p);
	}


	public static InteractionResult onRightClick(Player player, Level level, InteractionHand hand) {
		if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack held = player.getItemInHand(hand);
		if(EventHandler.canOpen(held)){
			EventHandler.tryOpen((ServerPlayer) player,held,null,true);
			return InteractionResult.SUCCESS_SERVER;
		}
		return InteractionResult.PASS;
	}
}
