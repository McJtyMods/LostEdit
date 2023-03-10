package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.network.LostEditMessages;
import mcjty.lib.typed.TypedMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerGui {

    // Action to perform when a confirmation is confirmed
    private final Map<UUID, Runnable> serverActions = new HashMap<>();
    // Action to perform when parameters are given
    private final Map<UUID, Consumer<TypedMap>> serverActionsWithParameters = new HashMap<>();

    // Ask for parameters before doing some server code
    public void askParameters(Player player, String message, TypedMap input, Consumer<TypedMap> action) {
        serverActionsWithParameters.put(player.getUUID(), action);
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketAskParameters(message, input));
    }

    // Ask for confirmation before doing some server code
    public void askConfirmation(Player player, String message, Runnable action) {
        serverActions.put(player.getUUID(), action);
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketAskConfirmation(message));
    }

    // Ask for a confirmation conditionally
    public void askConfirmationConditionally(Player player, boolean condition, String message, Runnable action) {
        if (condition) {
            askConfirmation(player, message, action);
        } else {
            action.run();
        }
    }


    // Show message
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

    public void confirm(Player player, TypedMap parameters) {
        Consumer<TypedMap> consumer = serverActionsWithParameters.get(player.getUUID());
        if (consumer != null) {
            consumer.accept(parameters);
            serverActionsWithParameters.remove(player.getUUID());
        }
    }

    public void cancel(Player player) {
        serverActions.remove(player.getUUID());
        serverActionsWithParameters.remove(player.getUUID());
    }

    public void openScreen(Player player, PacketOpenScreen.Screen screen) {
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketOpenScreen(screen));
    }
}
