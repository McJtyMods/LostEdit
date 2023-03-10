package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.client.PartsEditorScreen;
import com.mcjty.lostedit.client.ProjectScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenScreen {

    public static enum Screen {
        PROJECT(() -> ProjectScreen.open()),
        PARTEDITOR(() -> PartsEditorScreen.open());

        private final Runnable code;

        Screen(Runnable code) {
            this.code = code;
        }

        public Runnable getCode() {
            return code;
        }
    }

    private final Screen screen;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeShort(screen.ordinal());
    }

    public PacketOpenScreen(FriendlyByteBuf buf) {
        screen = Screen.values()[buf.readShort()];
    }

    public PacketOpenScreen(Screen screen) {
        this.screen = screen;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> screen.getCode().run());
        ctx.setPacketHandled(true);
    }
}
