package com.mcjty.lostedit.client.gui;

import com.mcjty.lostedit.LostEdit;
import mcjty.lib.gui.GuiItemScreen;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Label;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ShowMessage extends GuiItemScreen {

    private final String message;

    public ShowMessage(String message) {
        super(0, 0, ManualEntry.EMPTY);
        this.message = message;
    }

    @Override
    public void init() {
        window = new Window(this, new ResourceLocation(LostEdit.MODID, "gui/showmessage.gui"));
        super.init();
        ((Label)window.findChild("message")).text(message);
        ((Button)window.findChild("ok")).event(() -> {
            this.onClose();
        });
    }

    @Override
    protected void renderInternal(GuiGraphics poseStack, int mouseX, int mouseY, float ppartialTicks) {
        drawWindow(poseStack);
    }

    public static void open(String message) {
        Minecraft.getInstance().pushGuiLayer(new ShowMessage(message));
    }
}
