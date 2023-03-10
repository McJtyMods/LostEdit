package com.mcjty.lostedit.project;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import mcjty.lostcities.worldgen.lost.regassets.data.PartRef;

import java.util.List;
import java.util.Map;

public class ProjectData {

    public static final Codec<ProjectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, BuildingPartRE.CODEC).fieldOf("parts").forGetter(l -> l.parts)
            ).apply(instance, ProjectData::new));


    private final Map<String, BuildingPartRE> parts;

    public ProjectData(Map<String, BuildingPartRE> parts) {
        this.parts = parts;
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
}
