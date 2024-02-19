package com.mcjty.lostedit.network;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.ProjectInfo;
import com.mcjty.lostedit.client.ProjectInfoHolder;
import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PacketProjectInformationToClient(ProjectInfo info) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(LostEdit.MODID, "projectinfo");

    @Override
    public void write(FriendlyByteBuf buf) {
        info.toBytes(buf);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketProjectInformationToClient create(FriendlyByteBuf buf) {
        ProjectInfo info = ProjectInfo.fromBytes(buf);
        return new PacketProjectInformationToClient(info);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ProjectInfoHolder.setProjectInfo(info);
        });
    }
}
