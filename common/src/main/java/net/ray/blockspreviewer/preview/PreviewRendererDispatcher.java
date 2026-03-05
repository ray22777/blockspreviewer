package net.ray.blockspreviewer.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ray.blockspreviewer.config.Config;
import net.ray.blockspreviewer.config.ConfigGetter;
public class PreviewRendererDispatcher {
    private static Config config = ConfigGetter.getConfig();
    public static void renderTransparentBlock(PoseStack poseStack, BlockState state, BlockPos pos, Minecraft mc) { //TODO: add support for rendering block entities
        float alpha = config.transparentOpacity;

        if (state == null || mc.level == null) return;
        int light = LevelRenderer.getLightColor(mc.level, pos);

        VertexConsumer originalConsumer = mc.renderBuffers().bufferSource().getBuffer(RenderTypes.translucentMovingBlock());
        VertexConsumer alphaConsumer = createAlphaVertexConsumer(originalConsumer,ConfigGetter.getConfig().transparentOpacity);

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
        int color;
        try {
            long colorLong = Long.parseLong(config.outlineColor.replace("0x", "").replace("#", ""), 16);
            color = (int) colorLong;

        } catch (RuntimeException e) {
            color = 0xFFFFFFFF;
            System.err.println("Invalid outline color: " + config.outlineColor + ", using default");
        }
        if (mc.level != null) {
            int lightLevel = mc.level.getMaxLocalRawBrightness(pos);
            float brightness = 0.3f + (lightLevel / 15f) * 0.7f;

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            r = (int) (r * brightness);
            g = (int) (g * brightness);
            b = (int) (b * brightness);

            int alpha = (color >> 24) & 0xFF;
            if (alpha == 0) alpha = 0xFF;
            color = (alpha << 24) | (r << 16) | (g << 8) | b;
        }
        VertexConsumer vertexConsumer = mc.renderBuffers().bufferSource().getBuffer(RenderTypes.SECONDARY_BLOCK_OUTLINE);
        ShapeRenderer.renderShape(
                poseStack,
                vertexConsumer,
                shape,
                0, 0, 0,
                color,
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