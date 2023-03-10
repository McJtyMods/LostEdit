package com.mcjty.lostedit.client;

import net.minecraft.network.FriendlyByteBuf;

public record PartInfo(int height) {

    public static PartInfo fromBytes(FriendlyByteBuf buf) {
        int height = buf.readInt();
        return new PartInfo(height);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(height);
    }
}
