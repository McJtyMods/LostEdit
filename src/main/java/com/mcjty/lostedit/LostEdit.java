package com.mcjty.lostedit;

import com.mcjty.lostedit.project.ProjectManager;
import com.mcjty.lostedit.servergui.ServerGui;
import com.mcjty.lostedit.setup.ClientSetup;
import com.mcjty.lostedit.setup.ModSetup;
import com.mcjty.lostedit.setup.Registration;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

@Mod(LostEdit.MODID)
public class LostEdit {

    public static final String MODID = "lostedit";

    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();

    public static LostEdit instance;
    private final ProjectManager manager = new ProjectManager();
    private final ServerGui serverGui = new ServerGui();

    public LostEdit() {
        instance = this;
        Registration.register();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientSetup::initClient);
        });
    }

    public static ProjectManager manager() {
        return instance.manager;
    }

    public static ServerGui serverGui() {
        return instance.serverGui;
    }

    public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
        return instance.setup.tab(supplier);
    }
}
