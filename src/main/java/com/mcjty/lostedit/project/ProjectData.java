package com.mcjty.lostedit.project;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.lostcities.worldgen.lost.regassets.data.PartRef;

import java.util.List;

public class ProjectData {

    public static final Codec<ProjectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(PartRef.CODEC).fieldOf("parts").forGetter(l -> l.parts)
            ).apply(instance, ProjectData::new));


    private final List<PartRef> parts;

    public ProjectData(List<PartRef> parts) {
        this.parts = parts;
    }

    public List<PartRef> getParts() {
        return parts;
    }
}
