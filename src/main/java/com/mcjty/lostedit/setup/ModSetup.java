package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.compat.LostCitiesCompat;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.project.CommandHandler;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    public ModSetup() {
        createTab(LostEdit.MODID, "lostedit", () -> new ItemStack(Registration.EDITORWAND.get()));
    }

    @Override
    public void init(FMLCommonSetupEvent e) {
        LostEditMessages.registerMessages("lostedit");
        e.enqueueWork(() -> {
            CommandHandler.registerCommands();
        });
    }

    @Override
    protected void setupModCompat() {
        LostCitiesCompat.setupLostCities();
    }
}
