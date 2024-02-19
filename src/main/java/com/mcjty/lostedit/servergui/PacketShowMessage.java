package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.gui.ShowMessage;
import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketShowMessage(String message) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "showmessage");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketShowMessage create(FriendlyByteBuf buf) {
        return new PacketShowMessage(buf.readUtf(32767));
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ShowMessage.open(message);
        });
    }
}
