package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.client.AskParameters;
import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAskParameters {

    private final String message;
    private final TypedMap input;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        TypedMapTools.writeArguments(buf, input);
    }

    public PacketAskParameters(FriendlyByteBuf buf) {
        message = buf.readUtf(32767);
        input = TypedMapTools.readArguments(buf);
    }

    public PacketAskParameters(String message, TypedMap input) {
        this.message = message;
        this.input = input;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            AskParameters.open(message, input);
        });
        ctx.setPacketHandled(true);
    }
}
