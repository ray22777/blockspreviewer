package net.tricube.blocks_previewer.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.tricube.blocks_previewer.config.Config;
import net.tricube.blocks_previewer.preview.PreviewHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class BlockOutlineMixin {
	//? if <=1.21.1 && !fabric{

    @Inject(
        method = "renderHitOutline",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer consumer,
									Entity entity, double camX, double camY, double camZ,
									BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (Config.disableOutline.get() && Config.previewMode.get() == Config.PreviewMode.OUTLINE) {
            if (PreviewHandler.previewShowing) {
                ci.cancel();
            }
        }
    }
	//?}
}
