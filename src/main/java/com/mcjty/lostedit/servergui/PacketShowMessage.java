package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.client.gui.ShowMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketShowMessage {

    private final String message;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    public PacketShowMessage(FriendlyByteBuf buf) {
        message = buf.readUtf(32767);
    }

    public PacketShowMessage(String message) {
        this.message = message;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ShowMessage.open(message);
        });
        ctx.setPacketHandled(true);
    }
}
