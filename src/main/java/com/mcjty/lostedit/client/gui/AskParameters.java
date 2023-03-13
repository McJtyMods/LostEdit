package com.mcjty.lostedit.client.gui;

import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.servergui.PacketCancel;
import com.mcjty.lostedit.servergui.PacketConfirmParameters;
import com.mcjty.lostedit.servergui.ServerGui;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.*;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.List;

public class AskParameters extends GuiItemScreen implements IKeyReceiver {

    private final String message;
    private final List<ServerGui.Parameter> input;
    private final TypedMap.Builder builder = TypedMap.builder();

    public AskParameters(String message, List<ServerGui.Parameter> input) {
        super(LostEditMessages.INSTANCE, 0, 0, ManualEntry.EMPTY);
        this.message = message;
        this.input = input;
    }

    @Override
    public void init() {
        setWindowDimensions(300, input.size() * 19 + 18 + 18 + 18);
        super.init();

        Panel toplevel = Widgets.vertical(5, 2).filledRectThickness(2).filledBackground(0xff8899aa);
        toplevel.children(Widgets.label(this.message).desiredWidth(-1).desiredHeight(18));
        Widget focus = null;
        for (ServerGui.Parameter parameter : input) {
            Key<?> key = parameter.key();
            Panel horizontal = Widgets.horizontal().desiredWidth(-1).desiredHeight(18);
            toplevel.children(horizontal);
            horizontal.children(Widgets.label(key.name() + ": ").desiredWidth(50));
            if (key.type() == Type.BOOLEAN) {
                Boolean value = parameter.getValue(Type.BOOLEAN);
                ToggleButton button = new ToggleButton();
                horizontal.children(button.pressed(value).enabled(!parameter.readonly()).event(() -> {
                    builder.put((Key<Boolean>)key, button.isPressed());
                }));
                builder.put((Key<Boolean>)key, value);
            } else if (key.type() == Type.STRING) {
                String value = parameter.getValue(Type.STRING);
                TextField field;
                horizontal.children(field = new TextField().text(value).enabled(!parameter.readonly()).event((v) -> {
                    builder.put((Key<String>)key, v);
                }));
                if (focus == null) {
                    focus = field;
                }
                builder.put((Key<String>)key, value);
            } else if (key.type() == Type.INTEGER) {
                Integer value = parameter.getValue(Type.INTEGER);
                TextField field;
                horizontal.children(field = new TextField().text(value.toString()).enabled(!parameter.readonly()).event((v) -> {
                    builder.put((Key<Integer>)key, Integer.parseInt(v));
                }));
                if (focus == null) {
                    focus = field;
                }
                builder.put((Key<Integer>)key, value);
            } else if (key.type() == Type.DOUBLE) {
                Double value = parameter.getValue(Type.DOUBLE);
                TextField field;
                horizontal.children(field = new TextField().text(value.toString()).enabled(!parameter.readonly()).event((v) -> {
                    builder.put((Key<Double>)key, Double.parseDouble(v));
                }));
                if (focus == null) {
                    focus = field;
                }
                builder.put((Key<Double>)key, value);
            } else if (key.type() == Type.FLOAT) {
                Float value = parameter.getValue(Type.FLOAT);
                TextField field;
                horizontal.children(field = new TextField().text(value.toString()).enabled(!parameter.readonly()).event((v) -> {
                    builder.put((Key<Float>)key, Float.parseFloat(v));
                }));
                if (focus == null) {
                    focus = field;
                }
                builder.put((Key<Float>)key, value);
            } else if (key.type() == Type.LONG) {
                Long value = parameter.getValue(Type.LONG);
                TextField field;
                horizontal.children(field = new TextField().text(value.toString()).enabled(!parameter.readonly()).event((v) -> {
                    builder.put((Key<Long>)key, Long.parseLong(v));
                }));
                if (focus == null) {
                    focus = field;
                }
                builder.put((Key<Long>)key, value);
            } else {
                throw new RuntimeException("Unknown type!");
            }
        }
        Panel horizontal = Widgets.horizontal().desiredWidth(-1).desiredHeight(18);
        toplevel.children(horizontal);
        Button cancelButton = new Button().desiredWidth(50).desiredHeight(16).text("Cancel").event(() -> {
            network.sendToServer(new PacketCancel());
            super.onClose();
        });
        Button okButton = new Button().desiredWidth(50).desiredHeight(16).text("Ok").event(() -> {
            network.sendToServer(new PacketConfirmParameters(builder.build()));
            super.onClose();
        });
        horizontal.children(okButton, cancelButton);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
        if (focus != null) {
            window.setTextFocus(focus);
        }
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

    public static void open(String message, List<ServerGui.Parameter> input) {
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
