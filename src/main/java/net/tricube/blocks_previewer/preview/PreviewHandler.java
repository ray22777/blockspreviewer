package net.tricube.blocks_previewer.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
//?if >=26.1{
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.geometry.BakedQuad;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.tricube.blocks_previewer.config.Config;

import java.util.ArrayList;
import java.util.List;

public class PreviewHandler {
    private static BlockPos lastPreviewPos = null;
    private static List<BlockState> lastPreviewStates = new ArrayList<>();
    private static List<BlockPos> lastPreviewPositions = new ArrayList<>();
    private static InteractionHand lastUsedHand = InteractionHand.MAIN_HAND;
    public static boolean previewShowing = false;
	public static boolean isObstructed = false;
    public static void onRenderWorld(PoseStack poseStack
			//?if >=26.1
			 //,SubmitNodeCollector submitNodeCollector
	) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (lastPreviewStates.isEmpty()) return;
        var camera = mc.gameRenderer.getMainCamera();
		//~ if >=1.21.11 '.getPosition()' -> '.position()'
        Vec3 cameraPos = camera.getPosition();

        for (int i = 0; i < lastPreviewStates.size(); i++) {
            BlockState state = lastPreviewStates.get(i);
            BlockPos pos = lastPreviewPositions.get(i);

            poseStack.pushPose();
            double x = pos.getX() - cameraPos.x;
            double y = pos.getY() - cameraPos.y;
            double z = pos.getZ() - cameraPos.z;
            poseStack.translate(x, y, z);
			Config.PreviewMode previewMode = Config.previewMode.get();
            if(previewMode == Config.PreviewMode.TRANSPARENT){
                PreviewRenderer.renderTransparentBlock(poseStack, state, pos
						//?if >=26.1
						//,submitNodeCollector
						, mc);
            }
            else if(previewMode == Config.PreviewMode.OUTLINE){
                PreviewRenderer.renderBlockOutline(poseStack, state, pos, mc);
            }
            poseStack.popPose();
        }
    }
    public static void updatePreviewPosition() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            clearPreview();
			previewShowing = false;
            return;
        }

        ItemStack mainHandItem = mc.player.getMainHandItem();
        ItemStack offHandItem = mc.player.getOffhandItem();
        ItemStack previewItem = getPreviewItemFromHands(mainHandItem, offHandItem);

        if (previewItem.isEmpty()) {
            clearPreview();
			previewShowing = false;
            return;
        }

        HitResult hitResult = mc.hitResult;
        if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
            clearPreview();
			previewShowing = false;
            return;
        }

        BlockHitResult blockHit = (BlockHitResult) hitResult;
        if (!(previewItem.getItem() instanceof BlockItem blockItem)) {
            clearPreview();
			previewShowing = false;
            return;
        }

		BlockPlaceContext placeContext = new BlockPlaceContext(
				mc.player,
				lastUsedHand,
				previewItem,
				blockHit
		);

		placeContext = blockItem.updatePlacementContext(placeContext);
		if (placeContext == null || !placeContext.canPlace()) {
			clearPreview();
			previewShowing = false;
			return;
		}

		BlockPos placementPos = placeContext.getClickedPos();

		BlockState previewState = blockItem.getBlock().getStateForPlacement(placeContext);
		if (previewState == null) {
			clearPreview();
			previewShowing = false;
			return;
		}

		if (!previewState.canSurvive(mc.level, placementPos) ) {
			clearPreview();
			previewShowing = false;
			return;
		}
		//~ if >=1.21.11 '.of' -> '.placementContext'
		if(!mc.level.isUnobstructed(previewState, placementPos, CollisionContext.of(mc.player))){
			isObstructed = true;
		}
		else{
			isObstructed = false;
		} //maybe implement a red tint when obstructed

		updatePreviewData(placementPos, previewState, blockItem.getBlock(), mc, placeContext);
    }

    private static ItemStack getPreviewItemFromHands(ItemStack mainHand, ItemStack offHand) {
        if (PreviewManager.shouldShowPreview(mainHand)) {
            lastUsedHand = InteractionHand.MAIN_HAND;
            previewShowing = true;
            return mainHand;
        } else if (PreviewManager.shouldShowPreview(offHand)) {
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
