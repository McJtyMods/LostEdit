package com.mcjty.lostedit.servergui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class PacketCancel {

    public void toBytes(FriendlyByteBuf buf) {
    }

    public PacketCancel(FriendlyByteBuf buf) {
    }

    public PacketCancel() {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            serverGui().cancel(ctx.getSender());
        });
        ctx.setPacketHandled(true);
    }
}
