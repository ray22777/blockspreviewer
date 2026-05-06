package net.ray.quick_access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class Config {
	private static final String CONFIG_FILE = "config/quickaccess.properties";
	private static Properties config;

	public static void loadConfig() {
		config = new Properties();
		File configFile = new File(CONFIG_FILE);

		configFile.getParentFile().mkdirs();

		if (!configFile.exists()) {
			createDefaultConfig(configFile);
		}

		try (InputStream input = new FileInputStream(configFile)) {
			config.load(input);
			ModInit.LOGGER.info("[Quick Access] Config loaded");
		} catch (IOException e) {
			ModInit.LOGGER.warn("[Quick Access] Config error: " + e.getMessage());
			createDefaultConfig(configFile);
		}
	}

	private static void createDefaultConfig(File configFile) {
		try (PrintWriter writer = new PrintWriter(configFile)) {
			writer.println("# Quick Access configuration");
			writer.println("enableCraftingTable=true");
			writer.println("enableEnderChest=true");
			writer.println("enableShulkerBox=true");
			writer.println("enableLoom=true");
			writer.println("enableCartographyTable=true");
			writer.println("enableStonecutter=true");
			writer.println("enableGrindstone=true");
			writer.println("enableSmithingTable=true");
			writer.println("playShulkerSound=true");
			writer.println("playEnderChestSound=true");
			ModInit.LOGGER.info("[Quick Access] Created default config");
		} catch (IOException e) {
			ModInit.LOGGER.warn("[Quick Access] Failed to create config: " + e.getMessage());
		}
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = config.getProperty(key);
		return value != null ? Boolean.parseBoolean(value) : defaultValue;
	}

//	public static String getString(String key, String defaultValue) {
//		return config.getProperty(key, defaultValue);
//	}



//	public static int getInt(String key, int defaultValue) {
//		try {
//			String value = config.getProperty(key);
//			return value != null ? Integer.parseInt(value) : defaultValue;
//		} catch (NumberFormatException e) {
//			return defaultValue;
//		}
//	}
}
