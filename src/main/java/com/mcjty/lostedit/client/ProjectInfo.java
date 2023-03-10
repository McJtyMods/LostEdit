package com.mcjty.lostedit.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ProjectInfo(String projectName, String partName,
                          int partsGlobal, Map<String, PartInfo> partsProject, int partsListVersion,
                          ResourceKey<Level> editingAtDimension,
                          Integer editingAtChunkX, Integer editingAtChunkZ, Integer editingAtY) {

    public static final ProjectInfo EMPTY = new ProjectInfo("<none>", "<none>", 0, Collections.emptyMap(), 0,
            null, null, null, null);

    public boolean isEditing() {
        return editingAtChunkX != null;
    }

    public static ProjectInfo fromBytes(FriendlyByteBuf buf) {
        String projectName = buf.readUtf(32767);
        String partName = buf.readUtf(32767);
        int partsGlobal = buf.readInt();
        int size = buf.readInt();
        Map<String, PartInfo> partsProject = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String name = buf.readUtf(32767);
            partsProject.put(name, PartInfo.fromBytes(buf));
        }
        int partsListVersion = buf.readInt();
        boolean editing = buf.readBoolean();
        ResourceKey<Level> editingAtDimension = null;
        Integer editingAtChunkX = null;
        Integer editingAtChunkZ = null;
        Integer editingAtY = null;
        if (editing) {
            editingAtDimension = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
            editingAtChunkX = buf.readInt();
            editingAtChunkZ = buf.readInt();
            editingAtY = buf.readInt();
        }
        return new ProjectInfo(projectName, partName, partsGlobal, partsProject, partsListVersion, editingAtDimension, editingAtChunkX, editingAtChunkZ, editingAtY);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(projectName);
        buf.writeUtf(partName);
        buf.writeInt(partsGlobal);
        buf.writeInt(partsProject.size());
        for (var partInfo : partsProject.entrySet()) {
            buf.writeUtf(partInfo.getKey());
            partInfo.getValue().toBytes(buf);
        }
        buf.writeInt(partsListVersion);
        buf.writeBoolean(isEditing());
        if (editingAtChunkX != null) {
            buf.writeResourceLocation(editingAtDimension.location());
            buf.writeInt(editingAtChunkX);
            buf.writeInt(editingAtChunkZ);
            buf.writeInt(editingAtY);
        }
    }
}
