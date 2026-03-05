package net.ray.blockspreviewer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.ray.blockspreviewer.config.Config;
import net.ray.blockspreviewer.config.ConfigGetter;

public final class BlocksPreviewer {
    public static final String MOD_ID = "blocks_previewer";

    public static void init() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        ConfigGetter.config = AutoConfig.getConfigHolder(Config.class).getConfig();
    }
}
