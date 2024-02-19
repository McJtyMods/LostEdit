package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.gui.AskConfirmation;
import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PacketAskConfirmation(String message) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "askconfirmation");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketAskConfirmation create(FriendlyByteBuf buf) {
        return new PacketAskConfirmation(buf.readUtf(32767));
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            try {
                AskConfirmation.open(message);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
}
