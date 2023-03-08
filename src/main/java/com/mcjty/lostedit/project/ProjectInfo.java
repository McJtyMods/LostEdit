package com.mcjty.lostedit.project;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Collections;
import java.util.List;

public record ProjectInfo(String filename, int partsGlobal, List<PartInfo> partsProject) {

    public static ProjectInfo EMPTY = new ProjectInfo("", 0, Collections.emptyList());

    public static ProjectInfo fromBytes(FriendlyByteBuf buf) {
        String filename = buf.readUtf(32767);
        int partsGlobal = buf.readInt();
        int size = buf.readInt();
        List<PartInfo> partsProject = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            partsProject.add(PartInfo.fromBytes(buf));
        }
        return new ProjectInfo(filename, partsGlobal, partsProject);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(filename);
        buf.writeInt(partsGlobal);
        buf.writeInt(partsProject.size());
        for (PartInfo partInfo : partsProject) {
            partInfo.toBytes(buf);
        }
    }
}
