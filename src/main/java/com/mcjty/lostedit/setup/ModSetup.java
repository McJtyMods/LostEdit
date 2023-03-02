package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.compat.LostCitiesCompat;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    public ModSetup() {
        createTab(LostEdit.MODID, "lostedit", () -> new ItemStack(Registration.EDITORWAND.get()));
    }

    @Override
    public void init(FMLCommonSetupEvent e) {
    }

    @Override
    protected void setupModCompat() {
        LostCitiesCompat.setupLostCities();
    }
}
