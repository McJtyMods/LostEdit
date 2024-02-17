package com.mcjty.lostedit.setup;


import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.items.EditorWand;
import mcjty.lib.setup.DeferredItem;
import mcjty.lib.setup.DeferredItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.mcjty.lostedit.LostEdit.MODID;
import static com.mcjty.lostedit.LostEdit.tab;

public class Registration {

    public static final DeferredItems ITEMS = DeferredItems.create(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<EditorWand> EDITORWAND = ITEMS.register("editorwand", tab(() -> new EditorWand(createStandardProperties())));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        TABS.register(bus);
    }

    @NotNull
    public static Item.Properties createStandardProperties() {
        return LostEdit.setup.defaultProperties();
    }

    public static RegistryObject<CreativeModeTab> TAB = TABS.register("lostedit", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .icon(() -> new ItemStack(EDITORWAND.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((featureFlags, output) -> {
                LostEdit.setup.populateTab(output);
            })
            .build());
}
