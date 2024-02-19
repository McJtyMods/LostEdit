package com.mcjty.lostedit.client.gui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.ProjectInfoHolder;
import mcjty.lib.gui.*;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.WidgetList;
import mcjty.lib.gui.widgets.Widgets;
import mcjty.lib.varia.ClientTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class PartsEditorScreen extends GuiItemScreen implements IKeyReceiver {

    public PartsEditorScreen() {
        super(0, 0, ManualEntry.EMPTY);
    }

    private Label partWidget;
    private WidgetList partsList;
    private int partsListVersion = -1;

    @Override
    public void init() {
        window = new Window(this, new ResourceLocation(LostEdit.MODID, "gui/partseditor.gui"));
        super.init();
        ClientTools.enableKeyboardRepeat();

        partWidget = window.findChild("part");
        partsList = window.findChild("parts");
    }

    private void populateList() {
        if (partsListVersion >= ProjectInfoHolder.getProjectInfo().partsListVersion()) {
            return;
        }
        partsListVersion = ProjectInfoHolder.getProjectInfo().partsListVersion();
        partsList.removeChildren();
        for (var part : ProjectInfoHolder.getProjectInfo().partsProject().entrySet()) {
            partsList.children(Widgets.label(part.getKey()));
        }
    }

    @Override
    protected void renderInternal(GuiGraphics poseStack, int mouseX, int mouseY, float ppartialTicks) {
        partWidget.text(ProjectInfoHolder.getProjectInfo().partName());
        populateList();
        drawWindow(poseStack);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new PartsEditorScreen());
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
