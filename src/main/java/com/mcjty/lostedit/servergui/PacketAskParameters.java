package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.client.AskParameters;
import mcjty.lib.network.TypedMapTools;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketAskParameters {

    private final String message;
    private final List<ServerGui.Parameter> input;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeShort(input.size());
        for (ServerGui.Parameter parameter : input) {
            TypedMapTools.writeArgument(buf, parameter.key(), parameter.value());
        }
    }

    public PacketAskParameters(FriendlyByteBuf buf) {
        message = buf.readUtf(32767);
        int size = buf.readShort();
        input = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            TypedMapTools.readArgument(buf, (key, o) -> input.add(new ServerGui.Parameter(key, o)));
        }
    }

    public PacketAskParameters(String message, List<ServerGui.Parameter> input) {
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
