package com.mcjty.lostedit.project;

import net.minecraft.network.FriendlyByteBuf;

public record ProjectInfo(String filename, int partsGlobal, int partsProject) {

    public static ProjectInfo EMPTY = new ProjectInfo("", 0, 0);

    public static ProjectInfo fromBytes(FriendlyByteBuf buf) {
        String filename = buf.readUtf(32767);
        int partsGlobal = buf.readInt();
        int partsProject = buf.readInt();
        return new ProjectInfo(filename, partsGlobal, partsProject);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(filename);
        buf.writeInt(partsGlobal);
        buf.writeInt(partsProject);
    }
}
