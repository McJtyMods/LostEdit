package com.mcjty.lostedit.project;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProjectData {

    public record PaletteEntry(String key, BlockState state, boolean local) {}

    public static final Codec<PaletteEntry> PALETTE_ENTRY_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("key").forGetter(l -> l.key),
                    BlockState.CODEC.fieldOf("state").forGetter(l -> l.state),
                    Codec.BOOL.fieldOf("local").forGetter(l -> l.local)
            ).apply(instance, PaletteEntry::new));

    public static final Codec<ProjectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, BuildingPartRE.CODEC).fieldOf("parts").forGetter(l -> l.parts),
                    Codec.STRING.fieldOf("partName").forGetter(l -> l.partName),
                    Level.RESOURCE_KEY_CODEC.optionalFieldOf("editingAtDimension").forGetter(l -> Optional.ofNullable(l.editingAtDimension)),
                    Codec.INT.optionalFieldOf("editingAtChunkX").forGetter(l -> Optional.ofNullable(l.editingAtChunkX)),
                    Codec.INT.optionalFieldOf("editingAtChunkZ").forGetter(l -> Optional.ofNullable(l.editingAtChunkZ)),
                    Codec.INT.optionalFieldOf("editingAtY").forGetter(l -> Optional.ofNullable(l.editingAtY)),
                    Codec.unboundedMap(BlockPos.CODEC, Codec.STRING).fieldOf("palette").forGetter(l -> l.partData),
                    Codec.unboundedMap(Codec.STRING, PALETTE_ENTRY_CODEC).fieldOf("paletteMap").forGetter(l -> l.paletteMap)
            ).apply(instance, ProjectData::new));


    private final Map<String, BuildingPartRE> parts;
    private String partName = "";   // The part that is currently being edited
    private ResourceKey<Level> editingAtDimension = null;
    private Integer editingAtChunkX = null;
    private Integer editingAtChunkZ = null;
    private Integer editingAtY = null;
    private Map<BlockPos, String> partData = null;
    private Map<String, PaletteEntry> paletteMap = null;

    public ProjectData(Map<String, BuildingPartRE> parts, String partName,
                       Optional<ResourceKey<Level>> editingAtDimension,
                       Optional<Integer> editingAtChunkX, Optional<Integer> editingAtChunkZ, Optional<Integer> editingAtY,
                       Map<BlockPos, String> partData,
                       Map<String, PaletteEntry> paletteMap) {
        this.parts = parts;
        this.partName = partName;
        this.editingAtDimension = editingAtDimension.orElse(null);
        this.editingAtChunkX = editingAtChunkX.orElse(null);
        this.editingAtChunkZ = editingAtChunkZ.orElse(null);
        this.editingAtY = editingAtY.orElse(null);
        this.partData = partData;
        this.paletteMap = paletteMap;
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

    public Map<BlockPos, String> getPartData() {
        return partData;
    }

    public Map<String, PaletteEntry> getPaletteMap() {
        return paletteMap;
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
