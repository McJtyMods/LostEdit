package com.mcjty.lostedit.items;

import com.mcjty.lostedit.client.gui.ProjectScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EditorWand extends Item {

    public EditorWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            ProjectScreen.open();
        }
        return super.use(level, player, hand);
    }
}
