package net.ray.blocks_previewer.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//~ if >=26.1 '.world.WorldRenderEvents' -> '.level.LevelRenderEvents'
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.ray.blocks_previewer.config.Config;
import net.ray.blocks_previewer.preview.PreviewHandler;
import net.ray.blocks_previewer.preview.PreviewKeybindManager;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			PreviewHandler.updatePreviewPosition();
		});
		//? if >= 26.1{
		/*LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
			PreviewHandler.onRenderWorld(context.poseStack(), context.submitNodeCollector());
		});
		*///?}else{
		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, state)-> {
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
//?}
