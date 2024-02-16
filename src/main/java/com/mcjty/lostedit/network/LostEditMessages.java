package com.mcjty.lostedit.network;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.servergui.*;
import mcjty.lib.network.*;
import mcjty.lib.typed.TypedMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;

public class LostEditMessages {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void registerMessages(String name) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(LostEdit.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.registerMessage(id(), PacketProjectInformationToClient.class, PacketProjectInformationToClient::toBytes, PacketProjectInformationToClient::new, PacketProjectInformationToClient::handle);
        net.registerMessage(id(), PacketAskConfirmation.class, PacketAskConfirmation::toBytes, PacketAskConfirmation::new, PacketAskConfirmation::handle);
        net.registerMessage(id(), PacketShowMessage.class, PacketShowMessage::toBytes, PacketShowMessage::new, PacketShowMessage::handle);
        net.registerMessage(id(), PacketConfirm.class, PacketConfirm::toBytes, PacketConfirm::new, PacketConfirm::handle);
        net.registerMessage(id(), PacketConfirmParameters.class, PacketConfirmParameters::toBytes, PacketConfirmParameters::new, PacketConfirmParameters::handle);
        net.registerMessage(id(), PacketAskParameters.class, PacketAskParameters::toBytes, PacketAskParameters::new, PacketAskParameters::handle);
        net.registerMessage(id(), PacketCancel.class, PacketCancel::toBytes, PacketCancel::new, PacketCancel::handle);
        net.registerMessage(id(), PacketOpenScreen.class, PacketOpenScreen::toBytes, PacketOpenScreen::new, PacketOpenScreen::handle);

        PacketRequestDataFromServer.register(net, id());

        PacketHandler.registerStandardMessages(id(), net);
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendToServer(new PacketSendServerCommand(LostEdit.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        INSTANCE.sendToServer(new PacketSendServerCommand(LostEdit.MODID, command, TypedMap.EMPTY));
    }

    public static void sendToClient(Player player, String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendTo(new PacketSendClientCommand(LostEdit.MODID, command, argumentBuilder.build()), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(Player player, String command) {
        INSTANCE.sendTo(new PacketSendClientCommand(LostEdit.MODID, command, TypedMap.EMPTY), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
