package net.ray.blocks_previewer;

import net.ray.CraftConfig.platform.Platform;
import net.ray.blocks_previewer.config.Config;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import net.ray.blocks_previewer.platform.fabric.FabricPlatform;
//?} neoforge {
/*import net.ray.blocks_previewer.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import net.ray.blocks_previewer.platform.forge.ForgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ModInit {

	public static final String MOD_ID = /*$ mod_id*/ "blocks_previewer";
//	public static final String MOD_VERSION = /*$ mod_version*/ "0.1.0";
//	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Mod Template";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		Config.init();
//		LOGGER.info("Initializing {} on {}", MOD_ID, ModTemplate.xplat().loader());
//		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static void onInitializeClient() {
//		LOGGER.info("Initializing {} Client on {}", MOD_ID, ModTemplate.xplat().loader());
//		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		 *///?}
	}

	private static Identifier id(String path) {
		//? >=1.21 {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
		 //?} < 1.21 {
		/*return new Identifier(MOD_ID, path);
		*///?}
	}

	private static Identifier id(String namespace, String path) {
		//? >=1.21 {
		return Identifier.fromNamespaceAndPath(namespace, path);
		//?} < 1.21 {
		/*return new Identifier(namespace, path);
		*///?}
	}


}
