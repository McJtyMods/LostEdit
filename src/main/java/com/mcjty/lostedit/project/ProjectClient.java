package com.mcjty.lostedit.project;

import com.mcjty.lostedit.client.PartsEditorScreen;
import com.mcjty.lostedit.client.ProjectScreen;
import net.minecraft.client.Minecraft;

// Client side project data
public class ProjectClient {

    private static String filename;
    private static String partname;
    private static ProjectInfo info = ProjectInfo.EMPTY;

    public static void setFilename(String filename) {
        ProjectClient.filename = filename;
        if (Minecraft.getInstance().screen instanceof ProjectScreen projectScreen) {
            projectScreen.setFilename(filename);
        }
    }

    public static String getFilename() {
        return filename == null ? "" : filename;
    }

    public static void setPartname(String partname) {
        ProjectClient.partname = partname;
        if (Minecraft.getInstance().screen instanceof PartsEditorScreen partsEditorScreen) {
            partsEditorScreen.setPartname(partname);
        }
    }

    public static String getPartname() {
        return partname == null ? "" : partname;
    }

    public static void setProjectInfo(ProjectInfo info) {
        ProjectClient.info = info;
    }

    public static ProjectInfo getProjectInfo() {
        return info;
    }
}
