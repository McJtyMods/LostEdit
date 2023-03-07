package com.mcjty.lostedit.network;

import com.mcjty.lostedit.project.ProjectClient;
import com.mcjty.lostedit.project.ProjectInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketProjectInformationToClient {

    private final ProjectInfo info;

    public void toBytes(FriendlyByteBuf buf) {
        info.toBytes(buf);
    }

    public PacketProjectInformationToClient(FriendlyByteBuf buf) {
        info = ProjectInfo.fromBytes(buf);
    }

    public PacketProjectInformationToClient(ProjectInfo info) {
        this.info = info;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ProjectClient.setProjectInfo(info);
        });
        ctx.setPacketHandled(true);
    }
}
