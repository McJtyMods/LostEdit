package com.mcjty.lostedit.client;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.project.ProjectClient;
import com.mcjty.lostedit.setup.CommandHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.gui.*;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.ClientTools;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ProjectScreen extends GuiItemScreen implements IKeyReceiver {

    public ProjectScreen() {
        super(LostEditMessages.INSTANCE, 0, 0, ManualEntry.EMPTY);
    }

    @Override
    public void init() {
        window = new Window(this, LostEditMessages.INSTANCE, new ResourceLocation(LostEdit.MODID, "gui/project.gui"));
        super.init();
        window.event("file", (source, params) -> {
            network.sendToServer(new PacketSendServerCommand(LostEdit.MODID, CommandHandler.CMD_SETFILENAME, TypedMap.builder()
                    .put(CommandHandler.PARAM_FILENAME, ((TextField)source).getText())
                    .build()));
        });
        ((TextField)window.findChild("file")).text(ProjectClient.getFilename());
        ((Label)window.findChild("partsGlobal")).text("" + ProjectClient.getProjectInfo().partsGlobal());
        ((Label)window.findChild("partsProject")).text("" + ProjectClient.getProjectInfo().partsProject());
        ClientTools.enableKeyboardRepeat();
    }

    public void setFilename(String filename) {
        ((TextField)window.findChild("file")).text(filename);
    }

    @Override
    protected void renderInternal(PoseStack poseStack, int mouseX, int mouseY, float ppartialTicks) {
        drawWindow(poseStack);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new ProjectScreen());
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public void keyTypedFromEvent(int keyCode, int scanCode) {
        if (window != null) {
            if (window.keyTyped(keyCode, scanCode)) {
                super.keyPressed(keyCode, scanCode, 0); // @todo 1.14: modifiers?
            }
        }
    }

    @Override
    public void charTypedFromEvent(char codePoint) {
        if (window != null) {
            if (window.charTyped(codePoint)) {
                super.charTyped(codePoint, 0); // @todo 1.14: modifiers?
            }
        }
    }

    @Override
    public boolean mouseClickedFromEvent(double x, double y, int button) {
        WindowManager manager = getWindow().getWindowManager();
        manager.mouseClicked(x, y, button);
        return true;
    }

    @Override
    public boolean mouseReleasedFromEvent(double x, double y, int button) {
        WindowManager manager = getWindow().getWindowManager();
        manager.mouseReleased(x, y, button);
        return true;
    }

    @Override
    public boolean mouseScrolledFromEvent(double x, double y, double amount) {
        WindowManager manager = getWindow().getWindowManager();
        manager.mouseScrolled(x, y, amount);
        return true;
    }
}
