package net.ray.better_tab;

import net.minecraft.client.Minecraft;
//~ if >=26.1 '.GuiGraphics' -> '.GuiGraphicsExtractor'
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
//?if>=1.21
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class SinglePlayerTab {
	//~ if >=26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static void renderTab(GuiGraphicsExtractor guiGraphics){
        Minecraft mc = Minecraft.getInstance();
        if (mc.hasSingleplayerServer() && mc.player != null) {
            PlayerTabOverlay tabList = mc.gui.getTabList();
            if (tabList != null) {
                boolean tabPressed = mc.options.keyPlayerList.isDown();
                if (tabPressed) {
                    Scoreboard scoreboard = mc.level.getScoreboard();
					//?if>=1.21{
					Objective objective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
					//?}else{
					/*Objective objective = scoreboard.getDisplayObjective(0);
					*///?}
                    int screenWidth = mc.getWindow().getGuiScaledWidth();
                    tabList.setVisible(true);
					//~ if >=26.1 '.render(' -> '.extractRenderState('
                    tabList.extractRenderState(guiGraphics, screenWidth, scoreboard, objective);
                } else {
                    tabList.setVisible(false);
                }
            }
        }
    }
}
