package com.mcjty.lostedit.project;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.network.PacketAskConfirmation;
import com.mcjty.lostedit.network.PacketFilenameToClient;
import com.mcjty.lostedit.network.PacketShowMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProjectManager {

    // All UUID's are player id's
    public Map<UUID, Project> currentProjects = new HashMap<>();
    public Map<UUID, String> filenames = new HashMap<>();

    // Server side: action to perform when a confirmation is confirmed
    private final Map<UUID, Runnable> serverActions = new HashMap<>();

    // Server side: ask for confirmation
    public void askConfirmation(Player player, String message, Runnable action) {
        serverActions.put(player.getUUID(), action);
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketAskConfirmation(message));
    }

    // Server side: show message
    public void showMessage(Player player, String message) {
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketShowMessage(message));
    }

    public void confirm(Player player) {
        Runnable runnable = serverActions.get(player.getUUID());
        if (runnable != null) {
            runnable.run();
            serverActions.remove(player.getUUID());
        }
    }

    public void cancel(Player player) {
        serverActions.remove(player.getUUID());
    }

    public boolean hasProject(Player player) {
        return currentProjects.containsKey(player.getUUID());
    }

    // Server side, make new project
    public void newProject(Player player) {
        Project project = new Project();
        project.setFilename(filenames.computeIfAbsent(player.getUUID(),
                uuid -> uuid.toString().substring(0, 10)));
        currentProjects.put(player.getUUID(), project);
    }

    //; Server side, set filename
    public void setFilename(Player player, String filename) {
        String current = filenames.get(player.getUUID());
        if (!Objects.equals(filename, current)) {
            filenames.put(player.getUUID(), filename);
            if (currentProjects.containsKey(player.getUUID())) {
                currentProjects.get(player.getUUID()).setFilename(filename);
            }
            LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new PacketFilenameToClient(filename));
        }
    }
}
