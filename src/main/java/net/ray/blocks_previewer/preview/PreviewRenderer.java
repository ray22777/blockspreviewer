package net.ray.blocks_previewer.preview;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShapeRenderer;


import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.core.BlockPos;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ray.blocks_previewer.config.Config;

//?if >=26.1{
/*import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.ARGB;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;

import org.joml.Vector3fc;
*///?}

import java.awt.*;

public class PreviewRenderer {
	//?if >=26.1{
	/*private static ModelBlockRenderer modelBlockRenderer;
	private static final MovingBlockRenderState MOVING_BLOCK_STATE = new MovingBlockRenderState();

	public static void renderTransparentBlock(PoseStack poseStack, BlockState state, BlockPos pos,
											  SubmitNodeCollector collector, Minecraft mc) {
		if (state == null || mc.level == null) return;
		if (modelBlockRenderer == null) {
			modelBlockRenderer = new ModelBlockRenderer(true, true, BlockColors.createDefault());
		}

		float alpha = Config.transparentOpacity.get();
		int light = LevelRenderer.getLightCoords(mc.level, pos);

		MOVING_BLOCK_STATE.blockState = state;
		MOVING_BLOCK_STATE.blockPos = pos;
		MOVING_BLOCK_STATE.lightEngine = mc.level.getLightEngine();
		MOVING_BLOCK_STATE.biome = mc.level.getBiome(pos);
		BlockStateModel model = mc.getModelManager().getBlockStateModelSet().get(state);
		poseStack.pushPose();
		collector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, buffer) -> {
			VertexConsumer alphaConsumer = createAlphaVertexConsumer(buffer, alpha);
			modelBlockRenderer.tesselateBlock(
					(x, y, z, quad, instance) -> {
						for (int v = 0; v < 4; v++) {
							Vector3fc p = quad.position(v);
							long uv = quad.packedUV(v);
							float u = Float.intBitsToFloat((int)(uv >>> 32));
							float vf = Float.intBitsToFloat((int)(uv & 0xFFFFFFFFL));
							int c = instance.getColor(v);
							float r = ARGB.redFloat(c);
							float g = ARGB.greenFloat(c);
							float b = ARGB.blueFloat(c);
							alphaConsumer.addVertex(pose, p.x() + x, p.y() + y, p.z() + z)
									.setColor(r, g, b, alpha)
									.setUv(u, vf)
									.setUv2(light & 0xFFFF, (light >> 16) & 0xFFFF)
									.setNormal(pose, quad.direction().getStepX(),
											quad.direction().getStepY(),
											quad.direction().getStepZ());
						}
					},
					0f, 0f, 0f,
					MOVING_BLOCK_STATE,
					pos,
					state,
					model,
					42L
			);
		});
		poseStack.popPose();
	}

	*///?}else{
	public static void renderTransparentBlock(PoseStack poseStack, BlockState state, BlockPos pos, Minecraft mc) { //TODO: add support for rendering block entities
		float alpha = Config.transparentOpacity.get();

		if (state == null || mc.level == null) return;
		int light = LevelRenderer.getLightColor(mc.level, pos);

		VertexConsumer originalConsumer = mc.renderBuffers().bufferSource().getBuffer(RenderTypes.translucentMovingBlock());
		VertexConsumer alphaConsumer = createAlphaVertexConsumer(originalConsumer,alpha);

		poseStack.pushPose();
		try {

			mc.getBlockRenderer().renderSingleBlock(
					state,
					poseStack,
					renderType -> alphaConsumer,
					light,
					OverlayTexture.NO_OVERLAY
			);

		} finally {
			poseStack.popPose();
		}

		mc.renderBuffers().bufferSource().endBatch(RenderTypes.translucentMovingBlock());
	}
	//?}


    public static void renderBlockOutline(PoseStack poseStack, BlockState state, BlockPos pos, Minecraft mc) {
        if (state == null || mc.level == null) return;

        CollisionContext collisionContext = CollisionContext.of(mc.player);
        VoxelShape shape = state.getShape(mc.level, pos, collisionContext);
        if (shape.isEmpty()) {
            shape = state.getCollisionShape(mc.level, pos, collisionContext);
        }
        if (shape.isEmpty()) {
            shape = Shapes.block();
        }
        Color color;
		int finalColor = 0x00000000;
		color = Config.outlineColor.get();
        if (mc.level != null) {
            int lightLevel = mc.level.getMaxLocalRawBrightness(pos);
            float brightness = 0.3f + (lightLevel / 15f) * 0.7f;

			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();

			r = (int) (r * brightness);
			g = (int) (g * brightness);
			b = (int) (b * brightness);


			int alpha = color.getAlpha();
			if (alpha == 0) alpha = 0xFF;

			finalColor = (alpha << 24) | (r << 16) | (g << 8) | b;
        }
        VertexConsumer vertexConsumer = mc.renderBuffers().bufferSource().getBuffer(RenderTypes.SECONDARY_BLOCK_OUTLINE);
        ShapeRenderer.renderShape(
                poseStack,
                vertexConsumer,
                shape,
                0, 0, 0,
				finalColor,
                mc.getWindow().getAppropriateLineWidth()
        );
    }

    private static VertexConsumer createAlphaVertexConsumer(VertexConsumer original, float alphaMultiplier) {
        return new VertexConsumer() {
            @Override
            public VertexConsumer addVertex(float x, float y, float z) {
                return original.addVertex(x, y, z);
            }

            @Override
            public VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
                return original.addVertex(pose, x, y, z);
            }

            @Override
            public VertexConsumer setColor(int red, int green, int blue, int alphaValue) {
                int newAlpha = Math.max(0, Math.min(255, (int)(alphaValue * alphaMultiplier)));
                return original.setColor(red, green, blue, newAlpha);
            }

            @Override
            public VertexConsumer setColor(int color) {
                int a = (color >> 24) & 0xFF;
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                int newAlpha = Math.max(0, Math.min(255, (int)(a * alphaMultiplier)));
                int newColor = (newAlpha << 24) | (r << 16) | (g << 8) | b;
                return original.setColor(newColor);
            }

            @Override
            public VertexConsumer setColor(float red, float green, float blue, float alphaValue) {
                int newAlpha = Math.max(0, Math.min(255, (int)(alphaValue * 255 * alphaMultiplier)));
                return original.setColor(red, green, blue, newAlpha / 255.0f);
            }

            @Override
            public VertexConsumer setUv(float u, float v) {
                return original.setUv(u, v);
            }

            @Override
            public VertexConsumer setUv1(int u, int v) {
                return original.setUv1(u, v);
            }

            @Override
            public VertexConsumer setUv2(int u, int v) {
                return original.setUv2(u, v);
            }

            @Override
            public VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) {
                return original.setNormal(pose, x, y, z);
            }

            @Override
            public VertexConsumer setNormal(float x, float y, float z) {
                return original.setNormal(x, y, z);
            }

            @Override
            public VertexConsumer setLineWidth(float f) {
                return original.setLineWidth(f);
            }
        };
    }
}
