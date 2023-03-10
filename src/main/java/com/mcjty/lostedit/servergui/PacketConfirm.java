package com.mcjty.lostedit.servergui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class PacketConfirm {

    public void toBytes(FriendlyByteBuf buf) {
    }

    public PacketConfirm(FriendlyByteBuf buf) {
    }

    public PacketConfirm() {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            serverGui().confirm(ctx.getSender());
        });
        ctx.setPacketHandled(true);
    }
}
