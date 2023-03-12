package com.mcjty.lostedit.project;

import mcjty.lostcities.setup.CustomRegistries;
import mcjty.lostcities.worldgen.lost.cityassets.AssetRegistries;
import mcjty.lostcities.worldgen.lost.cityassets.BuildingPart;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class ProjectManager {

    // All UUID's are player id's
    private final Map<UUID, Project> currentProjects = new HashMap<>();

    public boolean hasProject(Player player) {
        return currentProjects.containsKey(player.getUUID());
    }

    public Project getProject(Player player) {
        return currentProjects.get(player.getUUID());
    }

    private void executeOnProject(Player player, Consumer<Project> runnable) {
        Project project = currentProjects.get(player.getUUID());
        if (project != null) {
            runnable.accept(project);
        }
    }

    private <T> T executeOnProjectWithResult(Player player, Function<Project, T> runnable, T defaultValue) {
        Project project = currentProjects.get(player.getUUID());
        if (project != null) {
            return runnable.apply(project);
        }
        return defaultValue;
    }

    // Make new project
    public void newProject(Player player, String projectName) {
        Project project = new Project();
        project.setProjectName(projectName);
        currentProjects.put(player.getUUID(), project);
        syncProjectToClient(player);
    }

    private void syncProjectToClient(Player player) {
        Project project = currentProjects.get(player.getUUID());
        if (project != null) {
            project.syncToClient(player);
        }
    }

    public void saveProject(Player player) {
        Project project = currentProjects.get(player.getUUID());
        if (project != null) {
            project.save(player);
        }
    }

    public void loadProject(Player player, String projectName) {
        newProject(player, projectName);
        Project project = currentProjects.get(player.getUUID());
        project.load(player);
    }

    public void newPart(Player player, String partName, int xSize, int zSize, int height) {
        executeOnProject(player, project -> project.newPart(player, partName, xSize, zSize, height, null));
    }

    public void deletePart(Player player, String part) {
        executeOnProject(player, project -> project.deletePart(player, part));
    }

    @Nullable
    public String getPart(Player player) {
        return executeOnProjectWithResult(player, Project::getPartName, null);
    }

    public void editPart(Player player) {
        executeOnProject(player, project -> project.editPart(player));
    }

    public void savePart(Player player) {
        executeOnProject(player, project -> project.savePart(player));
    }

    public void loadPart(Player player) {
        executeOnProject(player, project -> project.loadPart(player));
    }

    public void clonePart(Player player, String origPartName, String partName) {
        BuildingPart origPart = AssetRegistries.PARTS.get(player.level, new ResourceLocation(origPartName));
        if (origPart == null) {
            serverGui().showMessage(player, "Part '" + origPartName + "' does not exist!");
            return;
        }
        Registry<BuildingPartRE> registry = player.level.registryAccess().registryOrThrow(CustomRegistries.PART_REGISTRY_KEY);
        BuildingPartRE buildingPartRE = registry.get(ResourceKey.create(CustomRegistries.PART_REGISTRY_KEY, new ResourceLocation(origPartName)));
        if (buildingPartRE == null) {
            serverGui().showMessage(player, "Part '" + origPartName + "' does not exist!");
            return;
        }

        int xSize = origPart.getXSize();
        int zSize = origPart.getZSize();
        int height = origPart.getSliceCount();
        executeOnProject(player, project -> project.newPart(player, partName, xSize, zSize, height, buildingPartRE));
    }

    public boolean isEditing(Player player) {
        return executeOnProjectWithResult(player, Project::isEditing, false);
    }
}