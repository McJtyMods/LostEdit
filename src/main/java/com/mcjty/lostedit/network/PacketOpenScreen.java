package com.mcjty.lostedit.network;

import com.mcjty.lostedit.client.PartsEditorScreen;
import com.mcjty.lostedit.client.ProjectScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenScreen {

    public static final int SCREEN_PROJECT = 1;
    public static final int SCREEN_PARTEDITOR = 2;

    private final int screen;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeShort(screen);
    }

    public PacketOpenScreen(FriendlyByteBuf buf) {
        screen = buf.readShort();
    }

    public PacketOpenScreen(int screen) {
        this.screen = screen;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            switch (screen) {
                case SCREEN_PROJECT:
                    ProjectScreen.open();
                    break;
                case SCREEN_PARTEDITOR:
                    PartsEditorScreen.open();
                    break;
            }
        });
        ctx.setPacketHandled(true);
    }
}
