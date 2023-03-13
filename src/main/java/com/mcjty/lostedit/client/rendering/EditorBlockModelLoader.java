package com.mcjty.lostedit.client.rendering;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mcjty.lib.client.BaseGeometry;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.Collection;
import java.util.Collections;

public class EditorBlockModelLoader implements IGeometryLoader<EditorBlockModelLoader.EditorBlockGeometry> {

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register("editorblockloader", new EditorBlockModelLoader());
    }


    @Override
    public EditorBlockGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new EditorBlockGeometry();
    }

    public static class EditorBlockGeometry extends BaseGeometry<EditorBlockGeometry> {

        @Override
        public BakedModel bake() {
            return new EditorBlockBakedModel();
        }

        @Override
        public Collection<Material> getMaterials() {
            return Collections.emptyList();
        }
    }
}
