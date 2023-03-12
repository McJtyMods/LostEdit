package com.mcjty.lostedit.project;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.servergui.PacketOpenScreen;
import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;

import java.util.List;

import static com.mcjty.lostedit.LostEdit.manager;
import static com.mcjty.lostedit.LostEdit.serverGui;
import static com.mcjty.lostedit.servergui.ServerGui.parameter;

public class ProjectCommands {

    public static final String CMD_NEWPROJECT = "newproject";
    public static final String CMD_LOADPROJECT = "loadproject";
    public static final String CMD_SAVEPROJECT = "saveproject";
    public static final String CMD_SAVEDATA = "savedata";
    public static final String CMD_PARTSEDITOR = "partseditor";

    public static final Key<String> PARAM_PROJECTNAME = new Key<>("Name", Type.STRING);

    public static void registerCommands() {
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPROJECT, (player, arguments) -> {
            serverGui().askConfirmationConditionally(player, manager().hasProject(player), "Do you want to replace the current project?", () -> {
                serverGui().askParameters(player, "Enter a name for the new project", List.of(parameter(PARAM_PROJECTNAME, "")), result -> {
                    String projectName = result.get(PARAM_PROJECTNAME);
                    if (projectName == null || projectName.isEmpty()) {
                        serverGui().showMessage(player, "You must enter a name for the project!");
                    } else {
                        manager().newProject(player, projectName);
                    }
                });
            });
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_LOADPROJECT, (player, arguments) -> {
            serverGui().askConfirmationConditionally(player, manager().hasProject(player), "Do you want to replace the current project?", () -> {
                serverGui().askParameters(player, "Enter the name of the project to load", List.of(parameter(PARAM_PROJECTNAME, "")), result -> {
                    String projectName = result.get(PARAM_PROJECTNAME);
                    if (projectName == null || projectName.isEmpty()) {
                        serverGui().showMessage(player, "You must enter a name for the project!");
                    } else {
                        manager().loadProject(player, projectName);
                    }
                });
            });
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SAVEPROJECT, (player, arguments) -> {
            // @todo make a backup first
            manager().saveProject(player);
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SAVEDATA, (player, arguments) -> {
            serverGui().showMessage(player, "Not implemented yet!");
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_PARTSEDITOR, (player, arguments) -> {
            serverGui().openScreen(player, PacketOpenScreen.Screen.PARTEDITOR);
            return true;
        });
    }

}
