package net.ray.blocks_previewer.preview;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.ray.blocks_previewer.config.Config;


public class PreviewKeybindManager {
//    private static boolean wasFilterKeyDown = false;
//    private static boolean wasToggleKeyDown = false;
//    private static boolean isToggleKeyDown = false;
//    private static boolean isHoldKeyDown = false;
//    private static boolean isFilterKeyDown = false;


//    public static void handleInput() {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null) return;
//
//
//        if (!Config.enableMod.get()) return;
//
//        isToggleKeyDown = Keybinds.togglePreview.isDown();
//        isHoldKeyDown = Keybinds.holdPreview.isDown();
//        isFilterKeyDown = Keybinds.toggleFilter.isDown();
//        if (isToggleKeyDown && !wasToggleKeyDown) {
//            togglePreview();
//        }
//        wasToggleKeyDown = isToggleKeyDown;
//
//        if (isFilterKeyDown && !wasFilterKeyDown) {
//            toggleBlockFilter();
//        }
//        wasFilterKeyDown = isFilterKeyDown;
//    }

//    public static void togglePreview() {
//        config.togglePreview = !config.togglePreview;
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player != null) {
//            String status = config.togglePreview ? "§aON" : "§cOFF";
//            mc.player.displayClientMessage(Component.literal("§bBlocks Preview §7- " + status), true);
//        }
//    }

//    public static void toggleBlockFilter() {
//        config.blockFilter = !config.blockFilter;
//        AutoConfig.getConfigHolder(Config.class).save();
//
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player != null) {
//            String mode = config.blockFilter ? "§aFILTERED" : "§6ALL BLOCKS";
//            mc.player.displayClientMessage(Component.literal("§bBlock Filter §7- " + mode), true);
//        }
//    }

    public static boolean shouldShowPreview(ItemStack heldItem) {

        if (!Config.enableMod.get()) return false;

        boolean shouldShow = Config.togglePreview.get() ^ Config.holdPreview.get();
        if (!shouldShow) return false;

        if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
		if(Config.filterType.get() == Config.FilterType.CUSTOM){
			String heldId = BuiltInRegistries.BLOCK.getKey(((BlockItem) heldItem.getItem()).getBlock()).toString();
			if(Config.customFilterMode.get() == Config.FilterMode.WHITELIST){
				return Config.whitelistedBlocks.get().contains(heldId);
			}else{
				return !Config.blacklistedBlocks.get().contains(heldId);
			}
		}


        if (Config.filterType.get() == Config.FilterType.DIRECTIONAL) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            return hasFacingProperty(state);
        }

        return true;
    }

    public static boolean isDoorUpper(Block block) {
        return block instanceof DoorBlock;
    }

    public static boolean isTallPlantUpper(Block block) {
        return block instanceof DoublePlantBlock;
    }

    public static DoubleBlockHalf getUpperHalf() {
        return DoubleBlockHalf.UPPER;
    }

    private static boolean hasFacingProperty(BlockState state) {
        if (state.hasProperty(BlockStateProperties.FACING)) return true;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) return true;
        if (state.hasProperty(BlockStateProperties.AXIS)) return true;
        return false;
    }
}
