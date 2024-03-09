package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static com.mcjty.lostedit.LostEdit.serverGui;

public record PacketCancel() implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "cancel");

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketCancel create(FriendlyByteBuf buf) {
        return new PacketCancel();
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> serverGui().cancel(player));
        });
    }
}
