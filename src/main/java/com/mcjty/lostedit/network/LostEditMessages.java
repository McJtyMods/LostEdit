package com.mcjty.lostedit.network;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.servergui.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static mcjty.lib.network.PlayPayloadContext.wrap;

public class LostEditMessages {
    private static SimpleChannel INSTANCE;

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

        net.registerMessage(id(), PacketProjectInformationToClient.class, PacketProjectInformationToClient::write, PacketProjectInformationToClient::create, wrap(PacketProjectInformationToClient::handle));
        net.registerMessage(id(), PacketAskConfirmation.class, PacketAskConfirmation::write, PacketAskConfirmation::create, wrap(PacketAskConfirmation::handle));
        net.registerMessage(id(), PacketShowMessage.class, PacketShowMessage::write, PacketShowMessage::create, wrap(PacketShowMessage::handle));
        net.registerMessage(id(), PacketConfirm.class, PacketConfirm::write, PacketConfirm::create, wrap(PacketConfirm::handle));
        net.registerMessage(id(), PacketConfirmParameters.class, PacketConfirmParameters::write, PacketConfirmParameters::create, wrap(PacketConfirmParameters::handle));
        net.registerMessage(id(), PacketAskParameters.class, PacketAskParameters::write, PacketAskParameters::create, wrap(PacketAskParameters::handle));
        net.registerMessage(id(), PacketCancel.class, PacketCancel::write, PacketCancel::create, wrap(PacketCancel::handle));
        net.registerMessage(id(), PacketOpenScreen.class, PacketOpenScreen::write, PacketOpenScreen::create, wrap(PacketOpenScreen::handle));
    }

    public static <T> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <T> void sendToPlayer(T packet, Player player) {
        INSTANCE.sendTo(packet, ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
