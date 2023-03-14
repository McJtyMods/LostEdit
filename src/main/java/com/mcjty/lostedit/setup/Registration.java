package com.mcjty.lostedit.setup;


import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.items.EditorWand;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.mcjty.lostedit.LostEdit.MODID;
import static com.mcjty.lostedit.LostEdit.tab;

public class Registration {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<EditorWand> EDITORWAND = ITEMS.register("editorwand", tab(() -> new EditorWand(createStandardProperties())));

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    @NotNull
    public static Item.Properties createStandardProperties() {
        return LostEdit.setup.defaultProperties();
    }
}
