package net.ray.blockspreviewer;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.withDefaultNamespace("blockpreview"));
    public static final String TOGGLE = "key.blockpreview.toggle";
    public static final String HOLD = "key.blockpreview.hold";
    public static final String TOGGLE_FILTER = "key.blockpreview.toggle_filter";
    public static final int TOGGLE_KEY = GLFW.GLFW_KEY_G;
    public static final int HOLD_KEY = GLFW.GLFW_KEY_H;
    public static final int FILTER_KEY = GLFW.GLFW_KEY_J;
    public static KeyMapping togglePreview;
    public static KeyMapping holdPreview;
    public static KeyMapping toggleFilter;
}