package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.gui.PartsEditorScreen;
import com.mcjty.lostedit.client.gui.ProjectScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PacketOpenScreen(Screen screen) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "openscreen");

    public enum Screen {
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

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(screen.ordinal());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketOpenScreen create(FriendlyByteBuf buf) {
        Screen screen = Screen.values()[buf.readShort()];
        return new PacketOpenScreen(screen);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> screen.getCode().run());
    }
}
