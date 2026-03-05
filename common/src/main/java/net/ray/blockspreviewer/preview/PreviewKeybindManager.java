package net.ray.blockspreviewer.preview;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.ray.blockspreviewer.Keybinds;
import net.ray.blockspreviewer.config.Config;
import net.ray.blockspreviewer.config.ConfigGetter;

public class PreviewKeybindManager {
    private static Config config = ConfigGetter.getConfig();
    private static boolean wasFilterKeyDown = false;
    private static boolean wasToggleKeyDown = false;
    private static boolean isToggleKeyDown = false;
    private static boolean isHoldKeyDown = false;
    private static boolean isFilterKeyDown = false;


    public static void handleInput() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;


        if (!config.enableMod) return;

        isToggleKeyDown = Keybinds.togglePreview.isDown();
        isHoldKeyDown = Keybinds.holdPreview.isDown();
        isFilterKeyDown = Keybinds.toggleFilter.isDown();
        if (isToggleKeyDown && !wasToggleKeyDown) {
            togglePreview();
        }
        wasToggleKeyDown = isToggleKeyDown;

        if (isFilterKeyDown && !wasFilterKeyDown) {
            toggleBlockFilter();
        }
        wasFilterKeyDown = isFilterKeyDown;
    }

    public static void togglePreview() {
        config.togglePreview = !config.togglePreview;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            String status = config.togglePreview ? "§aON" : "§cOFF";
            mc.player.displayClientMessage(Component.literal("§bBlocks Preview §7- " + status), true);
        }
    }

    public static void toggleBlockFilter() {
        config.blockFilter = !config.blockFilter;
        AutoConfig.getConfigHolder(Config.class).save();

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            String mode = config.blockFilter ? "§aFILTERED" : "§6ALL BLOCKS";
            mc.player.displayClientMessage(Component.literal("§bBlock Filter §7- " + mode), true);
        }
    }

    public static boolean shouldShowPreview(ItemStack heldItem) {

        if (!config.enableMod) return false;

        boolean shouldShow = config.togglePreview || isHoldKeyDown;
        if (!shouldShow) return false;

        if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
            return false;
        }

        if (config.blockFilter) {
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