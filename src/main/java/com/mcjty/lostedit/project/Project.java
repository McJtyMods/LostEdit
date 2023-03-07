package com.mcjty.lostedit.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.network.PacketProjectInformationToClient;
import com.mojang.serialization.JsonOps;
import mcjty.lostcities.worldgen.lost.cityassets.AssetRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.io.*;
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

    public void save(Player player) {
        ProjectData.CODEC.encodeStart(JsonOps.INSTANCE, data).result().ifPresent(json -> {
            // Use Gson to convert the json to a string
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String s = gson.toJson(json);
            // Write s to a file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".json"));
                writer.write(s);
                writer.close();
                LostEdit.instance.manager.showMessage(player, "Saved project to '" + filename + ".json'");
            } catch (IOException e) {
                LostEdit.instance.manager.showMessage(player, "Error writing file '" + filename + ".json'!");
            }
        });
    }

    public void load(Player player) {
        // Read the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename + ".json"));
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
                LostEdit.instance.manager.showMessage(player, "Loaded project from '" + filename + ".json'");
            });
        } catch (IOException e) {
            LostEdit.instance.manager.showMessage(player, "Error reading file '" + filename + ".json'!");
        }
    }

    public void syncToClient(Player player) {
        int globalParts = AssetRegistries.PARTS.getNumAssets();
        ProjectInfo info = new ProjectInfo(filename, globalParts, data.getParts().size());
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketProjectInformationToClient(info));
    }
}
