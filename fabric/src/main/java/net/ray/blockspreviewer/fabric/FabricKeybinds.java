package net.ray.blockspreviewer.fabric;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.ray.blockspreviewer.Keybinds;
public class FabricKeybinds {


    public static void register() {
        Keybinds.togglePreview = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Keybinds.TOGGLE,
                InputConstants.Type.KEYSYM,
                Keybinds.TOGGLE_KEY,
                Keybinds.CATEGORY
        ));

        Keybinds.holdPreview = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Keybinds.HOLD,
                InputConstants.Type.KEYSYM,
                Keybinds.HOLD_KEY,
                Keybinds.CATEGORY
        ));

        Keybinds.toggleFilter = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Keybinds.TOGGLE_FILTER,
                InputConstants.Type.KEYSYM,
                Keybinds.FILTER_KEY,
                Keybinds.CATEGORY
        ));
    }
}