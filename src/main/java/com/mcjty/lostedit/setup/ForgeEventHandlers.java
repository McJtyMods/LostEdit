package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.project.Project;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.bus.api.SubscribeEvent;

import static com.mcjty.lostedit.LostEdit.manager;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide) {
                return;
            }
            if (manager().isEditing(player, event.getPos())) {
                Project project = manager().getProject(player);
                project.addBlock(player, event.getPos(), event.getPlacedBlock());
            }
        }
    }

    public void onDestroyBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide) {
            return;
        }
        if (manager().isEditing(player, event.getPos())) {
            Project project = manager().getProject(player);
            project.removeBlock(player, event.getPos());
        }
    }
}