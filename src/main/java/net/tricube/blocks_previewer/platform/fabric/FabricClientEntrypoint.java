package net.tricube.blocks_previewer.platform.fabric;

//? fabric {

/*import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//?if >=1.21.11{
//~ if >=26.1 '.world.WorldRenderEvents' -> '.level.LevelRenderEvents'
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
//?}else{
/^import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
^///?}


import net.tricube.blocks_previewer.ModInit;
import net.tricube.blocks_previewer.config.Config;
import net.tricube.blocks_previewer.preview.PreviewHandler;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
			ModInit.onInitializeClient();
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				PreviewHandler.updatePreviewPosition();
			});
			//? if >= 26.1{
			/^LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
				PreviewHandler.onRenderWorld(context.poseStack(), context.submitNodeCollector());
			});
			LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, state)-> {
				if(Config.disableOutline.get() && Config.previewMode.get() == Config.PreviewMode.OUTLINE){
					return !PreviewHandler.previewShowing;
				} else{
					return true;
				}
			});
			^///?}else{
			WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, state)-> {
				//~ if >=1.21.11 '.matrixStack()' -> '.matrices()'
				PreviewHandler.onRenderWorld(context.matrices());
				if(Config.disableOutline.get() && Config.previewMode.get() == Config.PreviewMode.OUTLINE){
					return !PreviewHandler.previewShowing;
				} else{
					return true;
				}
			});
			//?}



	}
}
*///?}
