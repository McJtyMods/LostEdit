package com.mcjty.lostedit.project;

import com.mcjty.lostedit.LostEdit;
import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;

import java.util.List;

import static com.mcjty.lostedit.LostEdit.manager;
import static com.mcjty.lostedit.LostEdit.serverGui;
import static com.mcjty.lostedit.servergui.ServerGui.parameter;
import static com.mcjty.lostedit.servergui.ServerGui.parameterRO;

public class PartCommands {

    public static final String CMD_NEWPART = "newpart";
    public static final String CMD_CLONEPART = "clonepart";
    public static final String CMD_DELETEPART = "deletepart";
    public static final String CMD_EDITPART = "editpart";

    public static final Key<String> PARAM_PARTNAME_ORIG = new Key<>("Original", Type.STRING);
    public static final Key<String> PARAM_PARTNAME = new Key<>("Name", Type.STRING);
    public static final Key<Integer> PARAM_HEIGHT = new Key<>("Height", Type.INTEGER);
    public static final Key<Integer> PARAM_XSIZE = new Key<>("X Size", Type.INTEGER);
    public static final Key<Integer> PARAM_ZSIZE = new Key<>("Z Size", Type.INTEGER);

    private static boolean isValidPartName(String partName) {
        if (partName == null || partName.isEmpty()) {
            return false;
        }
        if (partName.matches("[a-z0-9_]+:[a-z0-9_]+")) {
            return true;
        }
        return partName.matches("[a-z0-9_]+");
    }

    public static void registerCommands() {
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPART, (player, arguments) -> {
            serverGui().askParameters(player, "Give parameters for part",
                    List.of(parameter(PARAM_PARTNAME, ""),
                            parameter(PARAM_HEIGHT, 6),
                            parameterRO(PARAM_XSIZE, 16),
                            parameterRO(PARAM_ZSIZE, 16)),
                    params -> {
                        String partName = params.get(PARAM_PARTNAME);
                        if (!isValidPartName(partName)) {
                            serverGui().showMessage(player, "Invalid partname!");
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
            serverGui().askParameters(player, "Give parameters for part",
                    List.of(parameter(PARAM_PARTNAME_ORIG, ""),
                            parameter(PARAM_PARTNAME, "")),
                    params -> {
                        String origPartName = params.get(PARAM_PARTNAME_ORIG);
                        if (!isValidPartName(origPartName)) {
                            serverGui().showMessage(player, "Invalid original partname!");
                            return;
                        }
                        String partName = params.get(PARAM_PARTNAME);
                        if (!isValidPartName(partName)) {
                            serverGui().showMessage(player, "Invalid partname!");
                            return;
                        }
                        manager().clonePart(player, origPartName, partName);
                    });
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
        McJtyLib.registerCommand(LostEdit.MODID, CMD_EDITPART, (player, arguments) -> {
            manager().editPart(player);
            manager().loadPart(player);
            return true;
        });
    }
}
