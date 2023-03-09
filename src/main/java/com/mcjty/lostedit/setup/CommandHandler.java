package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.PartsEditorScreen;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.network.PacketOpenScreen;
import com.mcjty.lostedit.project.ProjectManager;
import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class CommandHandler {

    public static final String CMD_NEWPROJECT = "newproject";
    public static final String CMD_LOADPROJECT = "loadproject";
    public static final String CMD_SAVEPROJECT = "saveproject";
    public static final String CMD_SAVEDATA = "savedata";
    public static final String CMD_SETFILENAME = "setfilename";
    public static final String CMD_PARTSEDITOR = "partseditor";
    public static final String CMD_NEWPART = "newpart";
    public static final String CMD_CLONEPART = "clonepart";
    public static final String CMD_DELETEPART = "deletepart";
    public static final String CMD_SETPARTNAME = "setpartname";

    public static final Key<String> PARAM_FILENAME = new Key<>("filename", Type.STRING);
    public static final Key<String> PARAM_PARTNAME = new Key<>("partname", Type.STRING);

    public static void registerCommands() {
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPROJECT, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            if (manager.hasProject(player)) {
                manager.askConfirmation(player, "Do you want to replace the current project?", () -> {
                    manager.newProject(player);
                });
            } else {
                manager.newProject(player);
            }
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_LOADPROJECT, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            if (manager.hasProject(player)) {
                manager.askConfirmation(player, "Do you want to replace the current project?", () -> {
                    manager.loadProject(player);
                });
            } else {
                manager.loadProject(player);
            }
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SAVEPROJECT, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            if (manager.hasFilename(player)) {
                manager.askConfirmation(player, "Are you sure you want to replace '" + manager.getFilename(player) + "'?", () -> {
                    manager.saveProject(player);
                });
            } else {
                manager.showMessage(player, "No filename set. Please enter a filename first");
            }
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SAVEDATA, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            manager.showMessage(player, "Not implemented yet!");
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SETFILENAME, (player, arguments) -> {
            LostEdit.instance.manager.setFilename(player, arguments.get(PARAM_FILENAME));
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_PARTSEDITOR, (player, arguments) -> {
            LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketOpenScreen(PacketOpenScreen.SCREEN_PARTEDITOR));
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPART, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            manager.newPart(player);
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_CLONEPART, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            manager.showMessage(player, "Not implemented yet!");
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_DELETEPART, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            manager.deletePart(player);
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SETPARTNAME, (player, arguments) -> {
            ProjectManager manager = LostEdit.instance.manager;
            LostEdit.instance.manager.setPartname(player, arguments.get(PARAM_PARTNAME));
            return true;
        });
    }

}
