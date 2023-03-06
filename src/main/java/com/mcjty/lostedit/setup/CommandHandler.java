package com.mcjty.lostedit.setup;

import com.mcjty.lostedit.LostEdit;
import mcjty.lib.McJtyLib;

public class CommandHandler {

    public static final String CMD_NEWPROJECT = "newproject";
    public static final String CMD_LOADPROJECT = "loadproject";
    public static final String CMD_SAVEPROJECT = "saveproject";

    public static void registerCommands() {
        McJtyLib.registerCommand(LostEdit.MODID, CMD_NEWPROJECT, (player, arguments) -> {
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_LOADPROJECT, (player, arguments) -> {
            return true;
        });
        McJtyLib.registerCommand(LostEdit.MODID, CMD_SAVEPROJECT, (player, arguments) -> {
            return true;
        });
    }

}
