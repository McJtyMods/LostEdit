package com.mcjty.lostedit.client;

// Client side project data
public class ProjectInfoHolder {

    private static ProjectInfo info = ProjectInfo.EMPTY;

    public static void setProjectInfo(ProjectInfo info) {
        ProjectInfoHolder.info = info;
    }

    public static ProjectInfo getProjectInfo() {
        return info;
    }
}
