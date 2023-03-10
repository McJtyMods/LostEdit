package com.mcjty.lostedit.servergui;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class PacketConfirmParameters {

    private final TypedMap result;

    public void toBytes(FriendlyByteBuf buf) {
        TypedMapTools.writeArguments(buf, result);
    }

    public PacketConfirmParameters(FriendlyByteBuf buf) {
        result = TypedMapTools.readArguments(buf);
    }

    public PacketConfirmParameters(TypedMap result) {
        this.result = result;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            try {
                serverGui().confirm(ctx.getSender(), result);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        ctx.setPacketHandled(true);
    }
}
