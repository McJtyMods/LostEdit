package com.mcjty.lostedit.network;

import com.mcjty.lostedit.project.ProjectClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFilenameToClient {

    private final String filename;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(filename);
    }

    public PacketFilenameToClient(FriendlyByteBuf buf) {
        filename = buf.readUtf(32767);
    }

    public PacketFilenameToClient(String filename) {
        this.filename = filename;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ProjectClient.setFilename(filename);
        });
        ctx.setPacketHandled(true);
    }
}
