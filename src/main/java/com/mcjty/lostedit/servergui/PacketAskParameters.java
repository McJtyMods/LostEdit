package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.gui.AskParameters;
import mcjty.lib.network.TypedMapTools;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record PacketAskParameters(String message, List<ServerGui.Parameter> input) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "askparameters");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeShort(input.size());
        for (ServerGui.Parameter parameter : input) {
            buf.writeBoolean(parameter.readonly());
            TypedMapTools.writeArgument(buf, parameter.key(), parameter.value());
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketAskParameters create(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        int size = buf.readShort();
        List<ServerGui.Parameter> input = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            boolean readonly = buf.readBoolean();
            TypedMapTools.readArgument(buf, (key, o) -> input.add(new ServerGui.Parameter(key, o, readonly)));
        }
        return new PacketAskParameters(message, input);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            try {
                AskParameters.open(message, input);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
}
