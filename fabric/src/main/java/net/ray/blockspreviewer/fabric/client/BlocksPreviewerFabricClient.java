package net.ray.blockspreviewer.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.ray.blockspreviewer.config.Config;
import net.ray.blockspreviewer.config.ConfigGetter;
import net.ray.blockspreviewer.fabric.FabricKeybinds;
import net.ray.blockspreviewer.preview.PreviewKeybindManager;
import net.ray.blockspreviewer.preview.PreviewHandler;

public final class BlocksPreviewerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricKeybinds.register();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            FabricCommands.register(dispatcher);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PreviewKeybindManager.handleInput();
            PreviewHandler.updatePreviewPosition();
        });

        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context,state)-> {
            PreviewHandler.onRenderWorld(context.matrices());
            if(ConfigGetter.config.disableOutline && ConfigGetter.config.previewMode == Config.PreviewMode.OUTLINE){
                return !PreviewHandler.previewShowing;
            }
            else{
                return true;
            }

        });
    }
}
