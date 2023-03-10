package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.client.AskConfirmation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAskConfirmation {

    private final String message;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    public PacketAskConfirmation(FriendlyByteBuf buf) {
        message = buf.readUtf(32767);
    }

    public PacketAskConfirmation(String message) {
        this.message = message;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            AskConfirmation.open(message);
        });
        ctx.setPacketHandled(true);
    }
}
