package com.mcjty.lostedit.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.network.PacketProjectInformationToClient;
import com.mojang.serialization.JsonOps;
import mcjty.lostcities.worldgen.lost.cityassets.AssetRegistries;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class Project {

    private String projectName = "";
    private String partName = "";
    private ProjectData data;
    private int partListIndex = 0;

    public Project() {
        this.data = new ProjectData(new HashMap<>());
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void save(Player player) {
        ProjectData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().ifPresent(json -> {
            // Use Gson to convert the json to a string
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String s = gson.toJson(json);
            // Write s to a file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(projectName + ".json"));
                writer.write(s);
                writer.close();
                serverGui().showMessage(player, "Saved project to '" + projectName + ".json'");
            } catch (IOException e) {
                serverGui().showMessage(player, "Error writing file '" + projectName + ".json'!");
            }
        });
    }

    public void load(Player player) {
        // Read the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(projectName + ".json"));
            // Read the entire file into a string
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            // Use Gson to convert the string to json
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            // Use ProjectData.CODEC to convert the json to a ProjectData object
            ProjectData.CODEC.parse(JsonOps.INSTANCE, gson.fromJson(sb.toString(), JsonElement.class)).result().ifPresent(pair -> {
                // Set the data field to the new ProjectData object
                data = pair;
                serverGui().showMessage(player, "Loaded project from '" + projectName + ".json'");
            });
            syncToClient(player);
        } catch (IOException e) {
            serverGui().showMessage(player, "Error reading file '" + projectName + ".json'!");
        }
    }

    public void syncToClient(Player player) {
        int globalParts = AssetRegistries.PARTS.getNumAssets(player.level);
        ProjectInfo info = new ProjectInfo(projectName, partName, globalParts, data.getParts().keySet().stream().map(PartInfo::new).toList(), partListIndex);
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketProjectInformationToClient(info));
    }

    public void newPart(Player player, String partName, int xSize, int zSize, int height) {
        this.partName = partName;
        data.addPart(partName, new BuildingPartRE(xSize, zSize, new ArrayList<>(height), Optional.empty(), Optional.empty(), Optional.empty()));
        partListIndex++;
        syncToClient(player);
    }

    public void deletePart(Player player, String part) {
        data.removePart(part);
        partListIndex++;
        syncToClient(player);
    }

    public String getPartName() {
        return partName;
    }
}
