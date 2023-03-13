package com.mcjty.lostedit.setup;


import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.blocks.EditorBlockEntity;
import com.mcjty.lostedit.items.EditorWand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final RegistryObject<EditorWand> EDITORWAND = ITEMS.register("editorwand", tab(() -> new EditorWand(createStandardProperties())));

    public static final RegistryObject<Block> EDITOR_BLOCK = BLOCKS.register("editor_block", EditorBlockEntity::createBlock);
    public static final RegistryObject<Item> EDITOR_BLOCK_ITEM = ITEMS.register("editor_block", () -> new BlockItem(EDITOR_BLOCK.get(), createStandardProperties()));
    public static final RegistryObject<BlockEntityType<EditorBlockEntity>> EDITOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("editor_block", () -> BlockEntityType.Builder.of(EditorBlockEntity::new, EDITOR_BLOCK.get()).build(null));

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    @NotNull
    public static Item.Properties createStandardProperties() {
        return LostEdit.setup.defaultProperties();
    }
}
