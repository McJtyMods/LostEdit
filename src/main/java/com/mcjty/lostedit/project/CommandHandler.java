package com.mcjty.lostedit.project;

import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.servergui.PacketOpenScreen;
import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;

import static com.mcjty.lostedit.LostEdit.manager;
import static com.mcjty.lostedit.LostEdit.serverGui;

public class CommandHandler {

    public static final String CMD_NEWPROJECT = "newproject";
    public static final String CMD_LOADPROJECT = "loadproject";
    public static final String CMD_SAVEPROJECT = "saveproject";
    public static final String CMD_SAVEDATA = "savedata";
    public static final String CMD_PARTSEDITOR = "partseditor";
    public static final String CMD_NEWPART = "newpart";
    public static final String CMD_CLONEPART = "clonepart";
    public static final String CMD_DELETEPART = "deletepart";

    public static final Key<String> PARAM_PROJECTNAME = new Key<>("Name", Type.STRING);
    public static final Key<String> PARAM_PARTNAME = new Key<>("Name", Type.STRING);
    public static final Key<Integer> PARAM_HEIGHT = new Key<>("Height", Type.INTEGER);
    public static final Key<Integer> PARAM_XSIZE = new Key<>("X Size", Type.INTEGER);
    public static final Key<Integer> PARAM_ZSIZE = new Key<>("Z Size", Type.INTEGER);

    public static void registerCommands() {
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPROJECT, (player, arguments) -> {
            serverGui().askConfirmationConditionally(player, manager().hasProject(player), "Do you want to replace the current project?", () -> {
                serverGui().askParameters(player, "Enter a name for the new project", TypedMap.builder().put(PARAM_PROJECTNAME, "").build(), result -> {
                    String projectName = result.get(PARAM_PROJECTNAME);
                    if (projectName.isEmpty()) {
                        serverGui().showMessage(player, "You must enter a name for the project!");
                    }
                    manager().newProject(player, projectName);
                });
            });
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_LOADPROJECT, (player, arguments) -> {
            serverGui().askConfirmationConditionally(player, manager().hasProject(player), "Do you want to replace the current project?", () -> {
                serverGui().askParameters(player, "Enter the name of the project to load", TypedMap.builder().put(PARAM_PROJECTNAME, "").build(), result -> {
                    String projectName = result.get(PARAM_PROJECTNAME);
                    if (projectName.isEmpty()) {
                        serverGui().showMessage(player, "You must enter a name for the project!");
                    }
                    manager().loadProject(player, projectName);
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
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPART, (player, arguments) -> {
            serverGui().askParameters(player, "Give parameters for part",
                    TypedMap.builder()
                            .put(PARAM_PARTNAME, "")
                            .put(PARAM_HEIGHT, 6)
                            .put(PARAM_XSIZE, 16)
                            .put(PARAM_ZSIZE, 16)
                            .build(),
                    params -> {
                        String partName = params.get(PARAM_PARTNAME);
                        if (partName.isEmpty()) {
                            serverGui().showMessage(player, "Partname cannot be empty!");
                            return;
                        }
                        manager().newPart(player,
                                partName,
                                params.get(PARAM_XSIZE),
                                params.get(PARAM_ZSIZE),
                                params.get(PARAM_HEIGHT));
                    });
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_CLONEPART, (player, arguments) -> {
            serverGui().showMessage(player, "Not implemented yet!");
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_DELETEPART, (player, arguments) -> {
            String part = manager().getPart(player);
            if (part == null) {
                serverGui().showMessage(player, "No part selected!");
                return true;
            }
            serverGui().askConfirmation(player, "Are you sure you want to delete this part?", () -> {
                manager().deletePart(player, part);
            });
            return true;
        });
    }

}
