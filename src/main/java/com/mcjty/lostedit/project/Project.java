package com.mcjty.lostedit.project;

import java.util.ArrayList;

public class Project {

    private String filename;
    private ProjectData data;

    public Project() {
        this.data = new ProjectData(new ArrayList<>());
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
