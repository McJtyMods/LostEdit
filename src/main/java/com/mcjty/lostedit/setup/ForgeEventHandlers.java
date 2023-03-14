package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.project.Project;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mcjty.lostedit.LostEdit.manager;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level.isClientSide) {
                return;
            }
            if (manager().isEditing(player)) {
                Project project = manager().getProject(player);
                project.addBlock(player, event.getPos(), event.getPlacedBlock());
            }
        }
    }
}
