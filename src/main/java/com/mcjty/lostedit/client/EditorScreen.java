package com.mcjty.lostedit.client;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.setup.LostEditMessages;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.gui.GuiItemScreen;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.gui.Window;
import net.minecraft.resources.ResourceLocation;

public class EditorScreen extends GuiItemScreen {

    private static final int XSIZE = 390;
    private static final int YSIZE = 210;

    public EditorScreen() {
        super(LostEditMessages.INSTANCE, XSIZE, YSIZE, ManualEntry.EMPTY);
    }

    @Override
    public void init() {
        window = new Window(this, null, LostEditMessages.INSTANCE, new ResourceLocation(LostEdit.MODID, "gui/editorscreen.gui"));
        super.init();
    }

    @Override
    protected void renderInternal(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

    }
}
