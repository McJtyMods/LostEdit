package com.mcjty.lostedit.network;

import com.mcjty.lostedit.LostEdit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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
            LostEdit.instance.manager.confirm(ctx.getSender());
        });
        ctx.setPacketHandled(true);
    }
}
