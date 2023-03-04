package com.mcjty.lostedit.client;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.setup.LostEditMessages;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.gui.GuiItemScreen;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.gui.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ProjectScreen extends GuiItemScreen {

    public ProjectScreen() {
        super(LostEditMessages.INSTANCE, 0, 0, ManualEntry.EMPTY);
    }

    @Override
    public void init() {
        window = new Window(this, LostEditMessages.INSTANCE, new ResourceLocation(LostEdit.MODID, "gui/project.gui"));
        super.init();
    }

    @Override
    protected void renderInternal(PoseStack poseStack, int mouseX, int mouseY, float ppartialTicks) {
        drawWindow(poseStack);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ProjectScreen());
    }
}
