package com.mcjty.lostedit.project;

// Client side project data
public class ProjectClient {

    private static ProjectInfo info = ProjectInfo.EMPTY;

    public static void setProjectInfo(ProjectInfo info) {
        ProjectClient.info = info;
    }

    public static ProjectInfo getProjectInfo() {
        return info;
    }
}
