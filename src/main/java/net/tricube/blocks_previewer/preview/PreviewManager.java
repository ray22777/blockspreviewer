package net.tricube.blocks_previewer.preview;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.tricube.blocks_previewer.config.Config;


public class PreviewManager {

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

    private static boolean hasFacingProperty(BlockState state) {
        if (state.hasProperty(BlockStateProperties.FACING)) return true;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) return true;
        if (state.hasProperty(BlockStateProperties.AXIS)) return true;
        return false;
    }
}
