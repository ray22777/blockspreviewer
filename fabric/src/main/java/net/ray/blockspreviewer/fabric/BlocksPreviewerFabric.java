package net.ray.blockspreviewer.fabric;

import net.ray.blockspreviewer.BlocksPreviewer;
import net.fabricmc.api.ModInitializer;

public final class BlocksPreviewerFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        BlocksPreviewer.init();
    }
}
