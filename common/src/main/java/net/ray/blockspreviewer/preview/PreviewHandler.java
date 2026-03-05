package net.ray.blockspreviewer.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.ray.blockspreviewer.config.Config;
import net.ray.blockspreviewer.config.ConfigGetter;

import java.util.ArrayList;
import java.util.List;

public class PreviewHandler {
    private static BlockPos lastPreviewPos = null;
    private static List<BlockState> lastPreviewStates = new ArrayList<>();
    private static List<BlockPos> lastPreviewPositions = new ArrayList<>();
    private static InteractionHand lastUsedHand = InteractionHand.MAIN_HAND;
    public static boolean previewShowing = false;
    public static void onRenderWorld(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (lastPreviewStates.isEmpty()) return;
        var camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.position();

        for (int i = 0; i < lastPreviewStates.size(); i++) {
            BlockState state = lastPreviewStates.get(i);
            BlockPos pos = lastPreviewPositions.get(i);

            poseStack.pushPose();
            double x = pos.getX() - cameraPos.x;
            double y = pos.getY() - cameraPos.y;
            double z = pos.getZ() - cameraPos.z;
            poseStack.translate(x, y, z);
            Config.PreviewMode previewMode = ConfigGetter.config.previewMode;
            if(previewMode == Config.PreviewMode.TRANSPARENT){
                PreviewRendererDispatcher.renderTransparentBlock(poseStack, state, pos, mc);
            }
            else if(previewMode == Config.PreviewMode.OUTLINE){
                PreviewRendererDispatcher.renderBlockOutline(poseStack, state, pos, mc);
            }

            poseStack.popPose();
        }
    }

    public static void updatePreviewPosition() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            clearPreview();
            return;
        }

        ItemStack mainHandItem = mc.player.getMainHandItem();
        ItemStack offHandItem = mc.player.getOffhandItem();
        ItemStack previewItem = getPreviewItemFromHands(mainHandItem, offHandItem);

        if (previewItem.isEmpty()) {
            clearPreview();
            return;
        }

        HitResult hitResult = mc.hitResult;
        if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
            clearPreview();
            return;
        }

        BlockHitResult blockHit = (BlockHitResult) hitResult;
        BlockPos hitPos = blockHit.getBlockPos();
        BlockState hitBlockState = mc.level.getBlockState(hitPos);

        if (!(previewItem.getItem() instanceof BlockItem blockItem)) {
            clearPreview();
            return;
        }

        BlockPos placementPos;
        if (hitBlockState.canBeReplaced()) {
            placementPos = hitPos;
        } else {
            placementPos = hitPos.relative(blockHit.getDirection());
            if (!mc.level.getBlockState(placementPos).canBeReplaced()) {
                clearPreview();
                return;
            }
        }

        BlockPlaceContext placeContext = new BlockPlaceContext(
                mc.player,
                lastUsedHand,
                previewItem,
                new BlockHitResult(
                        Vec3.atCenterOf(placementPos),
                        blockHit.getDirection(),
                        placementPos,
                        blockHit.isInside()
                )
        );

        BlockState previewState = blockItem.getBlock().getStateForPlacement(placeContext);
        if (previewState == null) {
            previewState = blockItem.getBlock().defaultBlockState();
        }

        if (!canPlaceBlock(mc.level, placementPos, previewState, blockItem.getBlock(), placeContext)) {
            clearPreview();
            return;
        }

        updatePreviewData(placementPos, previewState, blockItem.getBlock(), mc, placeContext);
    }

    private static boolean canPlaceBlock(Level level, BlockPos pos, BlockState state,
                                         Block block, BlockPlaceContext context) {
        if (!level.getBlockState(pos).canBeReplaced()) {
            return false;
        }

        if (block instanceof DoorBlock || block instanceof DoublePlantBlock) {
            BlockPos upperPos = pos.above();
            if (!level.getBlockState(upperPos).canBeReplaced()) {
                return false;
            }
            return state.canSurvive(level, pos);
        }

        if (block instanceof BedBlock) {
            BlockPos headPos = pos.relative(context.getHorizontalDirection());
            if (!level.getBlockState(headPos).canBeReplaced()) {
                return false;
            }
            return state.canSurvive(level, pos) && state.canSurvive(level, headPos);
        }

        return state.canSurvive(level, pos);
    }
    private static ItemStack getPreviewItemFromHands(ItemStack mainHand, ItemStack offHand) {
        if (PreviewKeybindManager.shouldShowPreview(mainHand)) {
            lastUsedHand = InteractionHand.MAIN_HAND;
            previewShowing = true;
            return mainHand;
        } else if (PreviewKeybindManager.shouldShowPreview(offHand)) {
            lastUsedHand = InteractionHand.OFF_HAND;
            previewShowing = true;
            return offHand;
        }
        previewShowing = false;
        return ItemStack.EMPTY;
    }


    private static void updatePreviewData(BlockPos placementPos, BlockState previewState,
                                          Block block, Minecraft mc, BlockPlaceContext context) {
        lastPreviewPos = placementPos;
        lastPreviewStates.clear();
        lastPreviewPositions.clear();

        lastPreviewStates.add(previewState);
        lastPreviewPositions.add(placementPos);

        handleMultiBlockPlacement(block, previewState, placementPos, context);
    }

    private static void handleMultiBlockPlacement(Block block, BlockState previewState,
                                                  BlockPos placementPos, BlockPlaceContext context) {
        if (block instanceof DoorBlock) {
            BlockPos upperPos = placementPos.above();
            BlockState upperDoorState = previewState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
            lastPreviewStates.add(upperDoorState);
            lastPreviewPositions.add(upperPos);
        }
        else if (block instanceof DoublePlantBlock) {
            BlockPos upperPos = placementPos.above();
            BlockState upperPlantState = previewState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
            lastPreviewStates.add(upperPlantState);
            lastPreviewPositions.add(upperPos);
        }
        else if (block instanceof BedBlock) {
            // Get the head position based on facing direction
            BlockPos headPos = placementPos.relative(context.getHorizontalDirection());
            BlockState headState = previewState.setValue(BedBlock.PART, BedPart.HEAD);
            lastPreviewStates.add(headState);
            lastPreviewPositions.add(headPos);
        }
    }


    private static void clearPreview() {
        lastPreviewPos = null;
        lastPreviewStates.clear();
        lastPreviewPositions.clear();
    }
}