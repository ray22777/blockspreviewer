package net.tricube.blocks_previewer.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.tricube.CraftConfig.api.controller.*;
import net.tricube.CraftConfig.api.registry.CraftConfigRegistry;
import net.tricube.CraftConfig.api.v1.ConfigCategory;
import net.tricube.CraftConfig.api.v1.ConfigKeybinds;
import net.tricube.CraftConfig.api.v1.ConfigOption;
import net.tricube.CraftConfig.api.v1.ConfigSection;
import net.tricube.CraftConfig.api.v1.CraftConfig;
import net.tricube.CraftConfig.api.v1.entries.SelectionGridEntry;
import net.tricube.blocks_previewer.ModInit;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class Config {

	public static final ConfigOption<Boolean> enableMod =
			ConfigOption.booleanOption(Component.literal("Enable Mod"), true)
					.description(Component.literal("Enable mod"));

	public static final ConfigOption<Boolean> togglePreview =
			ConfigOption.booleanOption(Component.literal("Toggle Preview"), true)
					.description(Component.literal("Toggle between enabled and disabled"));

	public static final ConfigOption<Boolean> holdPreview =
			ConfigOption.booleanOption(Component.literal("Hold Preview"), false)
					.description(Component.literal("Hold to enable/disable preview"));



	public enum PreviewMode { TRANSPARENT, OUTLINE }
	public static final ConfigOption<PreviewMode> previewMode =
			ConfigOption.enumOption(Component.literal("Preview Mode"), PreviewMode.TRANSPARENT)
					.description(Component.literal("How the preview is shown."));

//	public static final ConfigOption<PreviewActivation> previewActivation =
//			ConfigOption.enumOption(Component.literal("Preview Activation"), PreviewActivation.class, PreviewActivation.TOGGLE)
//					.description(Component.literal("Conditions to enable the preview."));

	public static final ConfigOption<Float> transparentOpacity =
			ConfigOption.floatOption(Component.literal("Transparent Opacity"), 0.62f)
					.description(Component.literal("Opacity for transparent rendering."));



	public static final ConfigOption<Color> outlineColor =
			ConfigOption.colorOption(Component.literal("Outline Color"), new Color(Integer.decode("#A8FFF9")))
					.description(Component.literal("Color setting for outline rendering."));

	public static final ConfigOption<Color> obstructedOutline =
			ConfigOption.colorOption(Component.literal("Obstructed Color"), new Color(Integer.decode("#FF5555")))
					.description(Component.literal("Color to show on outline when it the block is obstructed."));


	public static final ConfigOption<Boolean> disableOutline =
			ConfigOption.booleanOption(Component.literal("Disable Vanilla Outline"), true)
					.description(Component.literal("Disable vanilla block outline when block placement is shown in outline mode."));


	public enum FilterType {ALL,DIRECTIONAL,CUSTOM}
	public static final ConfigOption<FilterType> filterType =
			ConfigOption.enumOption(Component.literal("Filter Type"), FilterType.ALL)
					.description(Component.literal("Which filter type to use."));

	public enum FilterMode { WHITELIST,BLACKLIST }
	public static final ConfigOption<FilterMode> customFilterMode =
			ConfigOption.enumOption(Component.literal("Filter Mode"), FilterMode.WHITELIST)
					.description(Component.literal("How the blocks are filtered when on custom."));


	public static  ConfigOption<java.util.List<String>> whitelistedBlocks =
			ConfigOption.<String>listOption(Component.literal("Whitelisted Blocks"), List.of());


	public static  ConfigOption<java.util.List<String>> blacklistedBlocks =
			ConfigOption.<String>listOption(Component.literal("Blacklisted Blocks"), List.of());


	public static final CraftConfig config = CraftConfig.create(ModInit.MOD_ID)
			.title(Component.literal("Blocks Previewer Config"))

			.category(ConfigCategory.builder(Component.literal("General"))
					.section(ConfigSection.builder(Component.literal("Preview"))
							.option(enableMod.controller(new BooleanController()))
							.option(togglePreview.controller(new BooleanController())
									.keybind(ConfigKeybinds.create()
											.defaultKey(InputConstants.KEY_G)
											.mode(ConfigKeybinds.Mode.TOGGLE)
											.notify(true)))
							.option(holdPreview.controller(new BooleanController())
									.keybind(ConfigKeybinds.create()
											.defaultKey(InputConstants.KEY_H)
											.mode(ConfigKeybinds.Mode.HOLD)
											.notify(false)))
							.build())
					.section(ConfigSection.builder(Component.literal("Rendering"))
							.option(previewMode.controller(new EnumController<>())
									.keybind(ConfigKeybinds.create()
											.defaultKey(InputConstants.UNKNOWN.getValue())
											.mode(ConfigKeybinds.Mode.TOGGLE)
											.notify(true)))
							.option(transparentOpacity.controller(new SliderController<>(0.0f, 1.0f)))
							.option(outlineColor.controller(new ColorController()))
							.option(disableOutline.controller(new BooleanController()))
//							.option(previewActivation.controller(new EnumController<>()))
							.build())
					.build())

			.category(ConfigCategory.builder(Component.literal("Block Filter"))
					.section(ConfigSection.builder(Component.literal("Filtering"))
							.option(filterType.controller(new EnumController<>())
									.keybind(ConfigKeybinds.create()
									.defaultKey(InputConstants.KEY_J)
									.notify(true)))
							.option(customFilterMode.controller(new EnumController<>()))
							.option(whitelistedBlocks
									.controller(new SelectionGridController(
											() -> SelectionGridEntry.forBlocks()
													.addAll(SelectionGridEntry.getAllBlockKeys())
													.sortAlphabetically()
													.build()
									)))
							.option(blacklistedBlocks
									.controller(new SelectionGridController(
											() -> SelectionGridEntry.forBlocks()
													.addAll(SelectionGridEntry.getAllBlockKeys())
													.sortAlphabetically()
													.build()
											,true
									)))
					.build())
			.build())
	.build();


	public static void init() {
		config.load();
		CraftConfigRegistry.register(ModInit.MOD_ID, config, "Blocks Previewer")
				.setModMenuEnabled(true)
				.setCommandEnabled(true)
				.setCustomCommand("blockspreviewer")
				.build();
	}
}
