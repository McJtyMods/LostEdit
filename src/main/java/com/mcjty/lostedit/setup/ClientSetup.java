package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.LostEdit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

public class ClientSetup {

    public static final ResourceLocation BLUE = new ResourceLocation(LostEdit.MODID, "block/effects/blue");

    public static void initClient(FMLClientSetupEvent event) {
    }

    public static List<ResourceLocation> onTextureStitch() {
        return List.of(BLUE);
    }
}
