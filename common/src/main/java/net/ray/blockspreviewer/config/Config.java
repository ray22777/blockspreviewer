package net.ray.blockspreviewer.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@me.shedaniel.autoconfig.annotation.Config(name = "blocks-previewer")
public class Config implements ConfigData {
    @ConfigEntry.Gui.Tooltip() //Enable mod
    public boolean enableMod = true;

    @ConfigEntry.Gui.Tooltip() //Toggle between enabled and disabled
    public boolean togglePreview = true;

    @ConfigEntry.Gui.Tooltip //Enable only when the block has multiple orientations/states.
    public boolean blockFilter = false;

    @ConfigEntry.Gui.Tooltip //How the preview is shown.
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PreviewMode previewMode = PreviewMode.TRANSPARENT;

    public enum PreviewMode {
        TRANSPARENT,
        OUTLINE;
    }

    @ConfigEntry.Gui.Tooltip //Conditions to enable the preview.
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PreviewActivation  previewActivation = PreviewActivation.TOGGLE;

    public enum PreviewActivation  {
        TOGGLE,
        HOLD;
    }

    @ConfigEntry.Gui.Tooltip() //Opacity for transparent renderin
    public float transparentOpacity = 0.62f;

    @ConfigEntry.Gui.Tooltip(count = 3) //Color setting for outline rendering.
                                        //Uses hex values, and supports alpha values infront.
                                        //E.g. an extra FF is added infront for 100% opacity
    public String outlineColor = "#A8FFF9";

    @ConfigEntry.Gui.Tooltip //Disable vanilla block outline when block placement is shown in outline mode.
    public boolean disableOutline = true;
}