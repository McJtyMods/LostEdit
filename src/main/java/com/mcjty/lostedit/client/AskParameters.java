package com.mcjty.lostedit.client;

import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.servergui.PacketCancel;
import com.mcjty.lostedit.servergui.PacketConfirmParameters;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.gui.*;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class AskParameters extends GuiItemScreen implements IKeyReceiver {

    private final String message;
    private final TypedMap input;
    private final TypedMap.Builder builder = TypedMap.builder();

    public AskParameters(String message, TypedMap input) {
        super(LostEditMessages.INSTANCE, 0, 0, ManualEntry.EMPTY);
        this.message = message;
        this.input = input;
    }

    @Override
    public void init() {
        setWindowDimensions(350, input.size() * 20 + 20 + 20 + 20);
        super.init();

        Panel toplevel = Widgets.vertical().filledRectThickness(2).filledBackground(0xff8899aa);
        toplevel.children(Widgets.label(this.message).desiredWidth(-1).desiredHeight(20));
        for (Key<?> key : input.getKeys()) {
            Panel horizontal = Widgets.horizontal().desiredWidth(-1).desiredHeight(20);
            toplevel.children(horizontal);
            horizontal.children(Widgets.label(key.name() + ": "));
            if (key.type() == Type.BOOLEAN) {
                Boolean value = input.get((Key<Boolean>)key);
                ToggleButton button = new ToggleButton();
                horizontal.children(button.pressed(value).event(() -> {
                    builder.put((Key<Boolean>)key, button.isPressed());
                }));
            } else if (key.type() == Type.STRING) {
                String value = input.get((Key<String>)key);
                horizontal.children(new TextField().text(value).event((v) -> {
                    builder.put((Key<String>)key, v);
                }));
            } else if (key.type() == Type.INTEGER) {
                Integer value = input.get((Key<Integer>)key);
                horizontal.children(new TextField().text(value.toString()).event((v) -> {
                    builder.put((Key<Integer>)key, Integer.parseInt(v));
                }));
            } else if (key.type() == Type.DOUBLE) {
                Double value = input.get((Key<Double>)key);
                horizontal.children(new TextField().text(value.toString()).event((v) -> {
                    builder.put((Key<Double>)key, Double.parseDouble(v));
                }));
            } else if (key.type() == Type.FLOAT) {
                Float value = input.get((Key<Float>)key);
                horizontal.children(new TextField().text(value.toString()).event((v) -> {
                    builder.put((Key<Float>)key, Float.parseFloat(v));
                }));
            } else if (key.type() == Type.LONG) {
                Long value = input.get((Key<Long>)key);
                horizontal.children(new TextField().text(value.toString()).event((v) -> {
                    builder.put((Key<Long>)key, Long.parseLong(v));
                }));
            } else {
                throw new RuntimeException("Unknown type!");
            }
        }
        Panel horizontal = Widgets.horizontal().desiredWidth(-1).desiredHeight(20);
        toplevel.children(horizontal);
        Button cancelButton = new Button().text("Cancel").event(() -> {
            network.sendToServer(new PacketCancel());
            super.onClose();
        });
        Button okButton = new Button().text("Ok").event(() -> {
            network.sendToServer(new PacketConfirmParameters(builder.build()));
            super.onClose();
        });
        horizontal.children(okButton, cancelButton);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
    }

    @Override
    public void onClose() {
        network.sendToServer(new PacketCancel());
        super.onClose();
    }

    @Override
    protected void renderInternal(PoseStack poseStack, int mouseX, int mouseY, float ppartialTicks) {
        drawWindow(poseStack);
    }

    public static void open(String message, TypedMap input) {
        Minecraft.getInstance().pushGuiLayer(new AskParameters(message, input));
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