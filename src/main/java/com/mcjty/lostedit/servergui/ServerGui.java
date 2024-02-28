package com.mcjty.lostedit.servergui;

import com.mcjty.lostedit.network.LostEditMessages;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerGui {

    // Action to perform when a confirmation is confirmed
    private final Map<UUID, Runnable> serverActions = new HashMap<>();
    // Action to perform when parameters are given
    private final Map<UUID, Consumer<TypedMap>> serverActionsWithParameters = new HashMap<>();

    public record Parameter(Key key, Object value, boolean readonly) {
        public <T> T getValue(Type<T> type) {
            return (T) value;
        }
    }
    public static <T> Parameter parameter(Key<T> key, T value) {
        return new Parameter(key, value, false);
    }

    public static <T> Parameter parameterRO(Key<T> key, T value) {
        return new Parameter(key, value, true);
    }

    // Ask for parameters before doing some server code
    public void askParameters(Player player, String message, List<Parameter> input, Consumer<TypedMap> action) {
        serverActionsWithParameters.put(player.getUUID(), action);
        LostEditMessages.sendToPlayer(new PacketAskParameters(message, input), player);
    }

    // Ask for confirmation before doing some server code
    public void askConfirmation(Player player, String message, Runnable action) {
        serverActions.put(player.getUUID(), action);
        LostEditMessages.sendToPlayer(new PacketAskConfirmation(message), player);
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
        LostEditMessages.sendToPlayer(new PacketShowMessage(message), player);
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
        LostEditMessages.sendToPlayer(new PacketOpenScreen(screen), player);
    }
}
