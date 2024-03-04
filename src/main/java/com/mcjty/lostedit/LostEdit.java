package com.mcjty.lostedit;

import com.mcjty.lostedit.client.rendering.EditorRenderer;
import com.mcjty.lostedit.project.ProjectManager;
import com.mcjty.lostedit.servergui.ServerGui;
import com.mcjty.lostedit.setup.ClientSetup;
import com.mcjty.lostedit.setup.ForgeEventHandlers;
import com.mcjty.lostedit.setup.ModSetup;
import com.mcjty.lostedit.setup.Registration;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fml.loading.FMLEnvironment;

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
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Dist dist = FMLEnvironment.dist;

        instance = this;
        Registration.register(bus);
        bus.addListener(setup::init);
        NeoForge.EVENT_BUS.register(new ForgeEventHandlers());

        if (dist.isClient()) {
            bus.addListener(ClientSetup::initClient);
            MinecraftForge.EVENT_BUS.addListener(EditorRenderer::render);
        }
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
