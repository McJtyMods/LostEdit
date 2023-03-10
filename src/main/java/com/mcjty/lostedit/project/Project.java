package com.mcjty.lostedit.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mcjty.lostedit.LostEdit;
import com.mcjty.lostedit.client.PartInfo;
import com.mcjty.lostedit.client.ProjectInfo;
import com.mcjty.lostedit.network.LostEditMessages;
import com.mcjty.lostedit.network.PacketProjectInformationToClient;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mcjty.lostcities.editor.EditorInfo;
import mcjty.lostcities.setup.Registration;
import mcjty.lostcities.varia.ComponentFactory;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.lost.BuildingInfo;
import mcjty.lostcities.worldgen.lost.cityassets.AssetRegistries;
import mcjty.lostcities.worldgen.lost.cityassets.BuildingPart;
import mcjty.lostcities.worldgen.lost.cityassets.CompiledPalette;
import mcjty.lostcities.worldgen.lost.cityassets.Palette;
import mcjty.lostcities.worldgen.lost.regassets.BuildingPartRE;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

import static com.mcjty.lostedit.LostEdit.serverGui;

public class Project {

    private String projectName = "";
    private ProjectData data;
    private int partListIndex = 0;

    public Project() {
        this.data = new ProjectData();
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
        Map<String, PartInfo> parts = new HashMap<>();
        for (Map.Entry<String, BuildingPartRE> entry : data.getParts().entrySet()) {
            BuildingPartRE part = entry.getValue();
            parts.put(entry.getKey(), new PartInfo(part.getSlices().length));
        }
        ProjectInfo info = new ProjectInfo(projectName, data.getPartName(), globalParts, parts,
                partListIndex,
                data.getEditingAtDimension(),
                data.getEditingAtChunkX(), data.getEditingAtChunkZ(), data.getEditingAtY());
        LostEditMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                new PacketProjectInformationToClient(info));
    }

    public void newPart(Player player, String partName, int xSize, int zSize, int height, @Nullable BuildingPartRE originalPart) {
        data.setPartName(partName);
        BuildingPartRE partRE;
        if (originalPart != null) {
            // Convert originalPart to json
            DataResult<JsonElement> result = BuildingPartRE.CODEC.encodeStart(JsonOps.INSTANCE, originalPart);
            // Convert json to BuildingPartRE
            DataResult<BuildingPartRE> result2 = BuildingPartRE.CODEC.parse(JsonOps.INSTANCE, result.result().get());
            partRE = result2.result().get();
        } else {
            List<List<String>> slices = new ArrayList<>(height);
            for (int y = 0; y < height; y++) {
                List<String> slice = new ArrayList<>(zSize);
                for (int x = 0; x < zSize; x++) {
                    // Create a string with xSize spaces
                    slice.add(StringUtils.repeat(" ", xSize));
                }
                slices.add(slice);
            }
            partRE = new BuildingPartRE(xSize, zSize, slices, Optional.empty(), Optional.empty(), Optional.empty());
        }

        data.addPart(partName, partRE);
        partListIndex++;
        syncToClient(player);
    }

    public void deletePart(Player player, String part) {
        data.removePart(part);
        partListIndex++;
        syncToClient(player);
    }

    public String getPartName() {
        return data.getPartName();
    }

    public void editPart(Player player) {
        ChunkPos pos = new ChunkPos(player.blockPosition());
        data.startEditing(player.level.dimension(), pos.x, pos.z, player.blockPosition().getY());
        syncToClient(player);
    }

    public void savePart(Player player) {
        // @todo
    }

    public void loadPart(Player player) {
        // We can assume that we are editing here
        BuildingPartRE partRE = data.getParts().get(getPartName());
        if (getPartName().contains(":")) {
            partRE.setRegistryName(new ResourceLocation(getPartName()));
        } else {
            partRE.setRegistryName(new ResourceLocation(LostEdit.MODID, getPartName()));
        }
        BuildingPart part = new BuildingPart(partRE);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        IDimensionInfo dimInfo = Registration.LOSTCITY_FEATURE.get().getDimensionInfo((ServerLevel)serverPlayer.level);
        if (dimInfo == null) {
            // Report error
            serverGui().showMessage(player, "Error: This is not a Lost City world!");
            return;
        }
        BlockPos pos = new BlockPos(data.getEditingAtChunkX() << 4, data.getEditingAtY(), data.getEditingAtChunkZ() << 4);
        startEditing(part, serverPlayer, pos, (ServerLevel)serverPlayer.level, dimInfo);
    }

    public boolean isEditing() {
        return data.isEditing();
    }


    public static void startEditing(BuildingPart part, ServerPlayer player, BlockPos start, ServerLevel level, IDimensionInfo dimInfo) {
        BuildingInfo info = BuildingInfo.getBuildingInfo(start.getX() >> 4, start.getZ() >> 4, dimInfo);
        CompiledPalette palette = info.getCompiledPalette();
        Palette partPalette = part.getLocalPalette(level);
        Palette buildingPalette = info.getBuilding().getLocalPalette(level);
        if (partPalette != null || buildingPalette != null) {
            palette = new CompiledPalette(palette, partPalette, buildingPalette);
        }

        EditorInfo editorInfo = EditorInfo.createEditorInfo(player.getUUID(), part.getName(), start);

        CompiledPalette finalPalette = palette;

        player.level.getServer().doRunTask(new TickTask(3, () -> {
            for (int y = 0; y < part.getSliceCount(); y++) {
                for (int x = 0; x < part.getXSize(); x++) {
                    for (int z = 0; z < part.getZSize(); z++) {
                        BlockPos pos = new BlockPos(info.chunkX * 16 + x, start.getY() + y, info.chunkZ * 16 + z);
                        Character character = part.getC(x, y, z);
                        BlockState state = finalPalette.get(character);
                        if (state != null) {
                            level.setBlock(pos, state, Block.UPDATE_ALL);
                        } else {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        }
                    }
                }
            }
            for (int y = 0; y < part.getSliceCount(); y++) {
                for (int x = 0; x < part.getXSize(); x++) {
                    for (int z = 0; z < part.getZSize(); z++) {
                        BlockPos pos = new BlockPos(info.chunkX * 16 + x, start.getY() + y, info.chunkZ * 16 + z);
                        Character character = part.getC(x, y, z);
                        if (finalPalette.get(character) != null) {
                            BlockState state = level.getBlockState(pos);
                            editorInfo.addPaletteEntry(character, state);
                        }
                    }
                }
            }
        }));
    }
}
