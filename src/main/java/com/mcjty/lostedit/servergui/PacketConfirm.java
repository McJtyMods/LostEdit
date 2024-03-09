package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static com.mcjty.lostedit.LostEdit.serverGui;

public record PacketConfirm() implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "confirm");

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketConfirm create(FriendlyByteBuf buf) {
        return new PacketConfirm();
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                try {
                    serverGui().confirm(player);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
