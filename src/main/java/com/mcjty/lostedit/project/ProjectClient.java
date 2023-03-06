package com.mcjty.lostedit.project;

import com.mcjty.lostedit.client.ProjectScreen;
import net.minecraft.client.Minecraft;

// Client side project data
public class ProjectClient {

    private static String filename;

    public static void setFilename(String filename) {
        ProjectClient.filename = filename;
        if (Minecraft.getInstance().screen instanceof ProjectScreen projectScreen) {
            projectScreen.setFilename(filename);
        }
    }

    public static String getFilename() {
        return filename == null ? "" : filename;
    }
}
