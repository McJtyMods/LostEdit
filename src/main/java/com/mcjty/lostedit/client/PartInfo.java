package com.mcjty.lostedit.client;

import net.minecraft.network.FriendlyByteBuf;

public record PartInfo(int height) {

    public static PartInfo fromBytes(FriendlyByteBuf buf) {
        return new PartInfo(buf.readInt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(height);
    }
}
