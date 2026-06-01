package net.tricube.blocks_previewer.platform.neoforge;

//? neoforge {

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
//? if >= 1.21.11
//import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
//? if >= 26.1
//import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.tricube.blocks_previewer.ModInit;
import net.tricube.blocks_previewer.config.Config;
import net.tricube.blocks_previewer.preview.PreviewHandler;


@EventBusSubscriber(modid = ModInit.MOD_ID)
public class NeoforgeEventHandler {

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		PreviewHandler.updatePreviewPosition();
	}
	//? if >= 26.1{
	/*@SubscribeEvent
	public static void onRenderWorld(SubmitCustomGeometryEvent event) {
		PreviewHandler.onRenderWorld(event.getPoseStack(),event.getSubmitNodeCollector());
	}

	*///?}else if >=1.21.11{
//	@SubscribeEvent
//	public static void onRenderWorld(RenderLevelStageEvent.AfterTranslucentBlocks event) {
//		PreviewHandler.onRenderWorld(event.getPoseStack());
// 	}
//?	}else{
		@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
			if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
				PreviewHandler.onRenderWorld(event.getPoseStack());
			}
	}
	//?}

//? if >=1.21.11{
//	@SubscribeEvent
//	public static void onRenderBlockHighlight(ExtractBlockOutlineRenderStateEvent event) {
//		if (Config.disableOutline.get() && Config.previewMode.get() == Config.PreviewMode.OUTLINE) {
//			if (PreviewHandler.previewShowing) {
//				event.setCanceled(true);
//			}
//		}
//	}
//?}
}
//?}
