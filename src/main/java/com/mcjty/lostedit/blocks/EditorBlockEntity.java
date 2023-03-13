package com.mcjty.lostedit.blocks;

import com.mcjty.lostedit.setup.Registration;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.varia.NBTTools;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;

import static mcjty.lib.builder.TooltipBuilder.*;

public class EditorBlockEntity extends GenericTileEntity {

    public static final ModelProperty<BlockState> BLOCKSTATE = new ModelProperty<>();

    private String paletteEntry;
    private BlockState state;

    public static BaseBlock createBlock() {
        return new BaseBlock(new BlockBuilder()
                .tileEntitySupplier(EditorBlockEntity::new)
                .infusable()
                .info(key("message.lostedit.shiftmessage"))
                .infoShift(header(), gold()));
    }

    public EditorBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.EDITOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(net, packet);

        if (level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            requestModelDataUpdate();
        }
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(BLOCKSTATE, getState())
                .build();
    }

    public void setPaletteEntry(String paletteEntry) {
        this.paletteEntry = paletteEntry;
        setChanged();
    }

    public String getPaletteEntry() {
        return paletteEntry;
    }

    public void setState(BlockState state) {
        this.state = state;
        markDirtyClient();
    }

    public BlockState getState() {
        return state;
    }

    @Override
    public void loadClientDataFromNBT(CompoundTag tagCompound) {
        super.loadClientDataFromNBT(tagCompound);
        CompoundTag info = tagCompound.getCompound("Info");
        state = NBTTools.readBlockState(info.getCompound("state"));
    }

    @Override
    public void saveClientDataToNBT(CompoundTag tagCompound) {
        super.saveClientDataToNBT(tagCompound);
        CompoundTag infoTag = getOrCreateInfo(tagCompound);
        infoTag.put("state", NbtUtils.writeBlockState(state));
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        CompoundTag info = tagCompound.getCompound("Info");
        paletteEntry = info.getString("paletteEntry");
        state = NBTTools.readBlockState(info.getCompound("state"));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        CompoundTag infoTag = getOrCreateInfo(tagCompound);
        infoTag.putString("paletteEntry", paletteEntry);
        infoTag.put("state", NbtUtils.writeBlockState(state));
    }

}
