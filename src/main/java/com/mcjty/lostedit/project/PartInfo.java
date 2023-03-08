package com.mcjty.lostedit.project;

import net.minecraft.network.FriendlyByteBuf;

public record PartInfo(String name) {

    public static PartInfo fromBytes(FriendlyByteBuf buf) {
        String name = buf.readUtf(32767);
        return new PartInfo(name);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(name);
    }
}
