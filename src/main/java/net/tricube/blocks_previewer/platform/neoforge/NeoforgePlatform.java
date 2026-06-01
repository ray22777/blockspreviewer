package net.tricube.blocks_previewer.platform.neoforge;

//? neoforge {

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.tricube.blocks_previewer.platform.Platform;

public class NeoforgePlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.NEOFORGE;
	}

	@Override
	public String mcVersion() {
		return "";
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader/*? if > 1.21.7 {*//*.getCurrent()*//*?}*/.isProduction();
	}
}
//?}
