package com.mcjty.lostedit.project;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Collections;
import java.util.List;

public record ProjectInfo(String projectName, String partName, int partsGlobal, List<PartInfo> partsProject, int partsListVersion) {

    public static final ProjectInfo EMPTY = new ProjectInfo("<none>", "<none>", 0, Collections.emptyList(), 0);

    public static ProjectInfo fromBytes(FriendlyByteBuf buf) {
        String projectName = buf.readUtf(32767);
        String partName = buf.readUtf(32767);
        int partsGlobal = buf.readInt();
        int size = buf.readInt();
        List<PartInfo> partsProject = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            partsProject.add(PartInfo.fromBytes(buf));
        }
        int partsListVersion = buf.readInt();
        return new ProjectInfo(projectName, partName, partsGlobal, partsProject, partsListVersion);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(projectName);
        buf.writeUtf(partName);
        buf.writeInt(partsGlobal);
        buf.writeInt(partsProject.size());
        for (PartInfo partInfo : partsProject) {
            partInfo.toBytes(buf);
        }
        buf.writeInt(partsListVersion);
    }
}
