package com.mcjty.lostedit.network;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.servergui.*;
import mcjty.lib.network.IPayloadRegistrar;
import mcjty.lib.network.Networking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;

public class LostEditMessages {

    private static IPayloadRegistrar registrar;

    public static void registerMessages() {
        registrar = Networking.registrar(LostEdit.MODID)
                .versioned("1.0")
                .optional();

        registrar.play(PacketProjectInformationToClient.class, PacketProjectInformationToClient::create, handler -> handler.server(PacketProjectInformationToClient::handle));
        registrar.play(PacketConfirmParameters.class, PacketConfirmParameters::create, handler -> handler.server(PacketConfirmParameters::handle));
        registrar.play(PacketCancel.class, PacketCancel::create, handler -> handler.server(PacketCancel::handle));

        registrar.play(PacketAskConfirmation.class, PacketAskConfirmation::create, handler -> handler.client(PacketAskConfirmation::handle));
        registrar.play(PacketShowMessage.class, PacketShowMessage::create, handler -> handler.client(PacketShowMessage::handle));
        registrar.play(PacketConfirm.class, PacketConfirm::create, handler -> handler.client(PacketConfirm::handle));
        registrar.play(PacketAskParameters.class, PacketAskParameters::create, handler -> handler.client(PacketAskParameters::handle));
        registrar.play(PacketOpenScreen.class, PacketOpenScreen::create, handler -> handler.client(PacketOpenScreen::handle));
    }

    public static <T> void sendToServer(T packet) {
        registrar.getChannel().sendToServer(packet);
    }

    public static <T> void sendToPlayer(T packet, Player player) {
        registrar.getChannel().sendTo(packet, ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
