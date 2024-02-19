package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static com.mcjty.lostedit.LostEdit.serverGui;

public record PacketConfirmParameters(TypedMap result) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "confirmparameters");

    @Override
    public void write(FriendlyByteBuf buf) {
        TypedMapTools.writeArguments(buf, result);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketConfirmParameters create(FriendlyByteBuf buf) {
        return new PacketConfirmParameters(TypedMapTools.readArguments(buf));
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                try {
                    serverGui().confirm(player, result);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
