package com.mcjty.lostedit.client.rendering;

import com.mcjty.lostedit.client.PartInfo;
import com.mcjty.lostedit.client.ProjectInfo;
import com.mcjty.lostedit.client.ProjectInfoHolder;
import com.mcjty.lostedit.setup.ClientSetup;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mcjty.lib.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class EditorRenderer {

    //    private static final RenderType TRANSLUCENT = RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
    private static final RenderType TRANSLUCENT = RenderType.translucent();

    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        ProjectInfo info = ProjectInfoHolder.getProjectInfo();
        if (info.isEditing()) {
            ResourceKey<Level> dimension = info.editingAtDimension();
            if (dimension == Minecraft.getInstance().level.dimension()) {
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

                MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                VertexConsumer builder = buffer.getBuffer(TRANSLUCENT);
                int x = info.editingAtChunkX() << 4;
                int z = info.editingAtChunkZ() << 4;
                var y = info.editingAtY();
                PartInfo partInfo = info.partsProject().get(info.partName());
                int height = partInfo.height();
                TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ClientSetup.BLUE);
                renderHighlightedOutline(poseStack, builder,
                        sprite,
                        x, y, z, x + 16, y + height, z + 16, 0.4f, 0.6f, 0.6f, 0.5f);

                poseStack.popPose();
                buffer.endBatch(TRANSLUCENT);
            }
        }
    }

    private static void renderHighlightedOutline(PoseStack poseStack, VertexConsumer buffer,
                                                 TextureAtlasSprite sprite,
                                                 float x1, float y1, float z1,
                                                 float x2, float y2, float z2,
                                                 float r, float g, float b, float a) {
        float m = .01f;
        RenderHelper.drawBox(poseStack, buffer, sprite, x1-m, x2+m, y1-m, y2+m, z1-m, z2+m, RenderHelper.FULLBRIGHT_SETTINGS);
        RenderHelper.drawBoxInside(poseStack, buffer, sprite, x1-m, x2+m, y1-m, y2+m, z1-m, z2+m, RenderHelper.FULLBRIGHT_SETTINGS);
    }
}