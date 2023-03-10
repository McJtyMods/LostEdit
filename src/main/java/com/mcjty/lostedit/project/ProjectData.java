package com.mcjty.lostedit.project;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import mcjty.lostcities.worldgen.lost.regassets.data.PartRef;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProjectData {

    public static final Codec<ProjectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, BuildingPartRE.CODEC).fieldOf("parts").forGetter(l -> l.parts),
                    Codec.STRING.fieldOf("partName").forGetter(l -> l.partName),
                    Level.RESOURCE_KEY_CODEC.optionalFieldOf("editingAtDimension").forGetter(l -> Optional.ofNullable(l.editingAtDimension)),
                    Codec.INT.optionalFieldOf("editingAtChunkX").forGetter(l -> Optional.ofNullable(l.editingAtChunkX)),
                    Codec.INT.optionalFieldOf("editingAtChunkZ").forGetter(l -> Optional.ofNullable(l.editingAtChunkZ)),
                    Codec.INT.optionalFieldOf("editingAtY").forGetter(l -> Optional.ofNullable(l.editingAtY))
            ).apply(instance, ProjectData::new));


    private final Map<String, BuildingPartRE> parts;
    private String partName = "";   // The part that is currently being edited
    private ResourceKey<Level> editingAtDimension = null;
    private Integer editingAtChunkX = null;
    private Integer editingAtChunkZ = null;
    private Integer editingAtY = null;

    public ProjectData(Map<String, BuildingPartRE> parts, String partName,
                       Optional<ResourceKey<Level>> editingAtDimension,
                       Optional<Integer> editingAtChunkX, Optional<Integer> editingAtChunkZ, Optional<Integer> editingAtY) {
        this.parts = parts;
        this.partName = partName;
        this.editingAtDimension = editingAtDimension.orElse(null);
        this.editingAtChunkX = editingAtChunkX.orElse(null);
        this.editingAtChunkZ = editingAtChunkZ.orElse(null);
        this.editingAtY = editingAtY.orElse(null);
    }

    public ProjectData() {
        this.parts = new HashMap<>();
        this.partName = "";
    }

    public Map<String, BuildingPartRE> getParts() {
        return parts;
    }

    public void addPart(String name, BuildingPartRE part) {
        parts.put(name, part);
    }

    public void removePart(String part) {
        parts.remove(part);
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void startEditing(ResourceKey<Level> dimension, int chunkX, int chunkZ, int y) {
        this.editingAtDimension = dimension;
        this.editingAtChunkX = chunkX;
        this.editingAtChunkZ = chunkZ;
        this.editingAtY = y;
    }

    public void stopEditing() {
        this.editingAtDimension = null;
        this.editingAtChunkX = null;
        this.editingAtChunkZ = null;
        this.editingAtY = null;
    }

    public boolean isEditing() {
        return editingAtChunkX != null;
    }

    public ResourceKey<Level> getEditingAtDimension() {
        return editingAtDimension;
    }

    public Integer getEditingAtChunkX() {
        return editingAtChunkX;
    }

    public Integer getEditingAtChunkZ() {
        return editingAtChunkZ;
    }

    public Integer getEditingAtY() {
        return editingAtY;
    }
}
