package com.mcjty.lostedit.network;

import com.mcjty.lostedit.project.ProjectClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCurrentNamesToClient {

    private final String filename;
    private final String partname;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(filename);
        buf.writeUtf(partname);
    }

    public PacketCurrentNamesToClient(FriendlyByteBuf buf) {
        filename = buf.readUtf(32767);
        partname = buf.readUtf(32767);
    }

    public PacketCurrentNamesToClient(String filename, String partname) {
        this.filename = filename;
        this.partname = partname;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ProjectClient.setFilename(filename);
            ProjectClient.setPartname(partname);
        });
        ctx.setPacketHandled(true);
    }
}
