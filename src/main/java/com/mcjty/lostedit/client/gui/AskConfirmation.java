package com.mcjty.lostedit.client.gui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.servergui.PacketCancel;
import com.mcjty.lostedit.servergui.PacketConfirm;
import mcjty.lib.gui.GuiItemScreen;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Label;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class AskConfirmation extends GuiItemScreen {

    private final String message;

    public AskConfirmation(String message) {
        super(LostEditMessages.INSTANCE, 0, 0, ManualEntry.EMPTY);
        this.message = message;
    }

    @Override
    public void init() {
        window = new Window(this, LostEditMessages.INSTANCE, new ResourceLocation(LostEdit.MODID, "gui/askconfirmation.gui"));
        super.init();
        ((Label)window.findChild("message")).text(message);
        ((Button)window.findChild("yes")).event(() -> {
            network.sendToServer(new PacketConfirm());
            super.onClose();
        });
        ((Button)window.findChild("no")).event(() -> {
            network.sendToServer(new PacketCancel());
            super.onClose();
        });
    }

    @Override
    public void onClose() {
        network.sendToServer(new PacketCancel());
        super.onClose();
    }

    @Override
    protected void renderInternal(GuiGraphics poseStack, int mouseX, int mouseY, float ppartialTicks) {
        drawWindow(poseStack);
    }

    public static void open(String message) {
        Minecraft.getInstance().pushGuiLayer(new AskConfirmation(message));
    }
}
