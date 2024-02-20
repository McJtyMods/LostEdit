package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.compat.LostCitiesCompat;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.project.PartCommands;
import com.mcjty.lostedit.project.ProjectCommands;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {


    @Override
    public void init(FMLCommonSetupEvent e) {
        LostEditMessages.registerMessages();
        e.enqueueWork(() -> {
            ProjectCommands.registerCommands();
            PartCommands.registerCommands();
        });
    }

    @Override
    protected void setupModCompat() {
        LostCitiesCompat.setupLostCities();
    }
}
